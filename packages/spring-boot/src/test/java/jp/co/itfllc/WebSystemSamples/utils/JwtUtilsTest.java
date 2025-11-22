package jp.co.itfllc.WebSystemSamples.utils;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * JwtUtilsクラスのユニットテスト
 */
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    // テスト用のRSAキーペア
    private static KeyPair testKeyPair;
    private static KeyPair anotherKeyPair;

    // テスト用の設定値
    private final String issuer = "test-issuer";
    private final long accessTokenExpireMinutes = 10;
    private final long refreshTokenExpireDays = 7;

    /**
     * テストの前にキーペアを生成し、JwtUtilsのインスタンスを初期化します。
     *
     * @throws NoSuchAlgorithmException
     */
    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        // 毎回同じキーペアを生成するために、インスタンスを再生成します。
        testKeyPair = generateRsaKeyPair();
        anotherKeyPair = generateRsaKeyPair();

        jwtUtils = new JwtUtils();

        // @Valueアノテーションで注入される値をリフレクションで設定します。
        ReflectionTestUtils.setField(
            jwtUtils,
            "privateKeyString",
            Base64.getEncoder().encodeToString(testKeyPair.getPrivate().getEncoded())
        );
        ReflectionTestUtils.setField(
            jwtUtils,
            "publicKeyString",
            Base64.getEncoder().encodeToString(testKeyPair.getPublic().getEncoded())
        );
        ReflectionTestUtils.setField(jwtUtils, "issuer", issuer);
        ReflectionTestUtils.setField(jwtUtils, "accessTokenExpireMinutes", accessTokenExpireMinutes);
        ReflectionTestUtils.setField(jwtUtils, "refreshTokenExpireDays", refreshTokenExpireDays);

        // init()メソッドを呼び出して、キーをロードします。
        jwtUtils.init();
    }

    /**
     * RSAキーペアを生成します。
     *
     * @return 生成されたKeyPair
     * @throws NoSuchAlgorithmException
     */
    private KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    @Test
    @DisplayName("init: 正常なキーで初期化が成功すること")
    void init_shouldInitializeKeys_whenKeysAreValid() {
        // setUpでinit()が呼ばれているため、キーがnullでないことを確認します。
        assertNotNull(ReflectionTestUtils.getField(jwtUtils, "privateKey"));
        assertNotNull(ReflectionTestUtils.getField(jwtUtils, "publicKey"));
    }

    @Test
    @DisplayName("init: 不正な秘密鍵でRuntimeExceptionがスローされること")
    void init_shouldThrowRuntimeException_whenPrivateKeyIsInvalid() {
        JwtUtils invalidJwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(invalidJwtUtils, "privateKeyString", "invalid-key");
        ReflectionTestUtils.setField(
            invalidJwtUtils,
            "publicKeyString",
            Base64.getEncoder().encodeToString(testKeyPair.getPublic().getEncoded())
        );

        // 不正なキーでinit()を呼び出すと、RuntimeExceptionがスローされることを確認します。
        assertThrows(RuntimeException.class, invalidJwtUtils::init);
    }

    @Test
    @DisplayName("generateAccessToken: アクセストークンが正常に生成されること")
    void generateAccessToken_shouldGenerateValidToken() {
        String subject = "test-user";
        Map<String, String> payload = new HashMap<>();
        payload.put("role", "user");

        String token = jwtUtils.generateAccessToken(subject, payload);

        assertNotNull(token);

        // 生成されたトークンを検証し、クレームが正しいことを確認します。
        Claims claims = jwtUtils.getClaimsFromAccessToken(token);
        assertEquals(issuer, claims.getIssuer());
        assertEquals(subject, claims.getSubject());
        assertEquals("user", claims.get("role", String.class));

        // 有効期限が正しく設定されていることを確認します。
        long expectedExpiration = Instant.now().plus(accessTokenExpireMinutes, ChronoUnit.MINUTES).toEpochMilli();
        assertTrue(claims.getExpiration().getTime() <= expectedExpiration + 1000); // 1秒の誤差を許容
        assertTrue(claims.getExpiration().getTime() > Instant.now().toEpochMilli());
    }

    @Test
    @DisplayName("generateRefreshToken: リフレッシュトークンが正常に生成されること")
    void generateRefreshToken_shouldGenerateValidToken() {
        String subject = "test-user-refresh";
        Map<String, String> payload = new HashMap<>();
        payload.put("tokenId", "refresh123");

        String token = jwtUtils.generateRefreshToken(subject, payload);

        assertNotNull(token);

        // 生成されたトークンを検証し、クレームが正しいことを確認します。
        Claims claims = jwtUtils.getClaimsFromAccessToken(token);
        assertEquals(issuer, claims.getIssuer());
        assertEquals(subject, claims.getSubject());
        assertEquals("refresh123", claims.get("tokenId", String.class));

        // 有効期限が正しく設定されていることを確認します。
        long expectedExpiration = Instant.now().plus(refreshTokenExpireDays, ChronoUnit.DAYS).toEpochMilli();
        assertTrue(claims.getExpiration().getTime() <= expectedExpiration + 1000); // 1秒の誤差を許容
    }

    @Test
    @DisplayName("getClaims: 有効なトークンからクレームが取得できること")
    void getClaims_shouldReturnClaims_whenTokenIsValid() {
        String subject = "test-subject";
        String token = jwtUtils.generateAccessToken(subject, Map.of("data", "test-data"));

        Claims claims = jwtUtils.getClaimsFromAccessToken(token);

        assertEquals(subject, claims.getSubject());
        assertEquals("test-data", claims.get("data", String.class));
    }

    @Test
    @DisplayName("getClaims: 期限切れのトークンでExpiredJwtExceptionがスローされること")
    void getClaims_shouldThrowExpiredJwtException_whenTokenIsExpired() {
        JwtUtils expiredJwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(
            expiredJwtUtils,
            "privateKeyString",
            Base64.getEncoder().encodeToString(testKeyPair.getPrivate().getEncoded())
        );
        ReflectionTestUtils.setField(
            expiredJwtUtils,
            "publicKeyString",
            Base64.getEncoder().encodeToString(testKeyPair.getPublic().getEncoded())
        );
        ReflectionTestUtils.setField(expiredJwtUtils, "issuer", issuer);
        // 有効期限を-1分に設定して、期限切れのトークンを生成します。
        ReflectionTestUtils.setField(expiredJwtUtils, "accessTokenExpireMinutes", -1L);
        ReflectionTestUtils.setField(expiredJwtUtils, "refreshTokenExpireDays", refreshTokenExpireDays);
        expiredJwtUtils.init();

        String expiredToken = expiredJwtUtils.generateAccessToken("test", new HashMap<>());

        // 期限切れトークンを検証すると、ExpiredJwtExceptionがスローされることを確認します。
        assertThrows(ExpiredJwtException.class, () -> jwtUtils.getClaimsFromAccessToken(expiredToken));
    }

    @Test
    @DisplayName("getClaims: 不正な署名のトークンでSignatureExceptionがスローされること")
    void getClaims_shouldThrowSignatureException_whenSignatureIsInvalid() throws NoSuchAlgorithmException {
        // 別のキーペアで署名されたトークンを生成します。
        JwtUtils anotherJwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(
            anotherJwtUtils,
            "privateKeyString",
            Base64.getEncoder().encodeToString(anotherKeyPair.getPrivate().getEncoded())
        );
        ReflectionTestUtils.setField(
            anotherJwtUtils,
            "publicKeyString",
            Base64.getEncoder().encodeToString(anotherKeyPair.getPublic().getEncoded())
        );
        ReflectionTestUtils.setField(anotherJwtUtils, "issuer", issuer);
        ReflectionTestUtils.setField(anotherJwtUtils, "accessTokenExpireMinutes", accessTokenExpireMinutes);
        ReflectionTestUtils.setField(anotherJwtUtils, "refreshTokenExpireDays", refreshTokenExpireDays);
        anotherJwtUtils.init();

        String tokenWithWrongSignature = anotherJwtUtils.generateAccessToken("test", new HashMap<>());

        // 本来の公開鍵で検証すると、SignatureExceptionがスローされることを確認します。
        assertThrows(SignatureException.class, () -> jwtUtils.getClaimsFromAccessToken(tokenWithWrongSignature));
    }

    @Test
    @DisplayName("generateToken: ペイロードが空でもトークンが生成されること")
    void generateToken_shouldSucceed_withEmptyPayload() {
        String subject = "user-no-payload";
        String token = jwtUtils.generateAccessToken(subject, new HashMap<>());

        assertNotNull(token);
        Claims claims = jwtUtils.getClaimsFromAccessToken(token);
        assertEquals(subject, claims.getSubject());
    }

    @Test
    @DisplayName("generateToken: ペイロードに複雑なオブジェクトが含まれていてもトークンが生成されること")
    void generateToken_shouldSucceed_withComplexPayload() {
        String subject = "user-complex-payload";
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", 123);
        payload.put("roles", new String[] { "ADMIN", "USER" });
        Map<String, String> details = new HashMap<>();
        details.put("department", "Engineering");
        payload.put("details", details);

        String token = jwtUtils.generateAccessToken(subject, payload);

        assertNotNull(token);
        Claims claims = jwtUtils.getClaimsFromAccessToken(token);
        assertEquals(subject, claims.getSubject());
        assertEquals(123, claims.get("userId", Integer.class));
        assertNotNull(claims.get("roles"));
        assertNotNull(claims.get("details"));
        assertEquals("Engineering", ((Map<?, ?>) claims.get("details")).get("department"));
    }

    @Test
    @DisplayName("getClaims: トークンの発行者(issuer)が異なると例外がスローされること")
    void getClaims_shouldFail_whenIssuerIsDifferent() {
        // 異なる発行者でトークンを生成
        JwtUtils anotherIssuerJwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(
            anotherIssuerJwtUtils,
            "privateKeyString",
            Base64.getEncoder().encodeToString(testKeyPair.getPrivate().getEncoded())
        );
        ReflectionTestUtils.setField(
            anotherIssuerJwtUtils,
            "publicKeyString",
            Base64.getEncoder().encodeToString(testKeyPair.getPublic().getEncoded())
        );
        ReflectionTestUtils.setField(anotherIssuerJwtUtils, "issuer", "another-issuer");
        ReflectionTestUtils.setField(anotherIssuerJwtUtils, "accessTokenExpireMinutes", accessTokenExpireMinutes);
        anotherIssuerJwtUtils.init();

        String token = anotherIssuerJwtUtils.generateAccessToken("test", new HashMap<>());

        // 本来のissuerを要求するjwtUtilsで検証すると例外がスローされる
        Exception exception = assertThrows(io.jsonwebtoken.IncorrectClaimException.class, () -> {
            jwtUtils.getClaimsFromAccessToken(token);
        });

        assertTrue(exception.getMessage().contains("Expected iss"));
    }
}
