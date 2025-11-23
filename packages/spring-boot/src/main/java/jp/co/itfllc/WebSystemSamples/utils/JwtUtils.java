package jp.co.itfllc.WebSystemSamples.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT (JSON Web Token) の生成、検証、および関連する操作を行うためのユーティリティクラスです。
 */
@Component
public class JwtUtils {

    /**
     * JSONとJavaオブジェクトのマッピングを行うためのマッパーです。
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 暗号学的に安全な乱数を生成するためのジェネレータです。
     */
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * URLセーフなBase64エンコーディングを行うためのエンコーダーです。
     */
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    /**
     * アクセストークンの署名に使用する、Base64エンコードされたPKCS#8形式の秘密鍵です。
     */
    @Value("${app.jwt.secret-private-key}")
    private String privateKeyString;

    /**
     * アクセストークンの検証に使用する、Base64エンコードされたX.509形式の公開鍵です。
     */
    @Value("${app.jwt.secret-public-key}")
    private String publicKeyString;

    /**
     * JWTの発行者（issuer）を示す文字列です。
     */
    @Value("${app.jwt.issuer}")
    private String issuer;

    /**
     * アクセストークンの有効期間を分単位で設定します。
     */
    @Value("${app.jwt.access-token-expire-minutes}")
    private long accessTokenExpireMinutes;

    /**
     * リフレッシュトークンの有効期間を日単位で設定します。
     */
    @Value("${app.jwt.refresh-token-expire-days}")
    private long refreshTokenExpireDays;

    /**
     * アクセストークンの署名に使用する `PrivateKey` オブジェクトです。
     */
    private PrivateKey accessPrivateKey;

    /**
     * アクセストークンの検証に使用する `PublicKey` オブジェクトです。
     */
    private PublicKey accessPublicKey;

    /**
     * コンポーネントの初期化時に呼び出され、プロパティから読み込んだ鍵文字列を `PrivateKey` および `PublicKey` オブジェクトに変換します。
     */
    @PostConstruct
    public void init() {
        try {
            this.accessPrivateKey = this.loadPrivateKey(this.privateKeyString);
            this.accessPublicKey = this.loadPublicKey(this.publicKeyString);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // アプリケーションの起動時にキーの読み込みに失敗した場合は、致命的なエラーとして処理します。
            throw new RuntimeException("Failed to load JWT keys", e);
        }
    }

    /**
     * 指定されたサブジェクトとペイロードを基に、新しいアクセストークンを生成します。
     *
     * @param <T>     ペイロードの型
     * @param subject トークンの主題（通常はユーザーIDなど）。
     * @param payload トークンに含めるカスタムクレームのペイロード。
     * @return 生成された署名付きアクセストークン（JWS）。
     */
    public <T> String generateAccessToken(final String subject, final T payload) {
        final long expirationMillis = this.accessTokenExpireMinutes * 60 * 1000;

        return this.generateToken(subject, payload, expirationMillis, this.accessPrivateKey);
    }

    /**
     * 暗号学的に安全なランダムなバイト配列を生成し、それをBase64エンコードしてリフレッシュトークンとして返します。
     *
     * @return 生成されたランダムなリフレッシュトークン文字列。
     */
    public String generateRefreshToken() {
        final var randomBytes = new byte[64];
        this.secureRandom.nextBytes(randomBytes);

        return this.base64Encoder.encodeToString(randomBytes);
    }

    /**
     * 指定されたアクセストークンを検証し、そのペイロード（クレーム）を抽出します。
     *
     * @param token 検証対象のアクセストークン。
     * @return トークンから抽出されたクレーム情報。
     */
    public Claims getClaimsFromAccessToken(final String token) {
        return this.getClaims(token, this.accessPublicKey);
    }

    /**
     * リフレッシュトークンをSHA-512アルゴリズムでハッシュ化し、その結果をバイト配列として返します。
     *
     * @param token ハッシュ化するリフレッシュトークン文字列。
     * @return ハッシュ化されたトークンのバイト配列。
     * @throws Exception MessageDigestのインスタンス化に失敗した場合。
     */
    public byte[] hashRefreshToken(final String token) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-512");
        final byte[] hash = digest.digest(token.getBytes());

        return hash;
    }

    /**
     * 現在の日時から設定された有効期間（日数）を加算し、リフレッシュトークンの有効期限を計算して返します。
     *
     * @return 計算された有効期限の `OffsetDateTime` オブジェクト。
     */
    public OffsetDateTime getExpiresAt() {
        return OffsetDateTime.now().plusDays(this.refreshTokenExpireDays);
    }

    /**
     * Base64エンコードされたPKCS#8形式の文字列から `PrivateKey` オブジェクトを生成します。
     *
     * @param keyString デコードして処理する秘密鍵の文字列。
     * @return 生成された `PrivateKey` インスタンス。
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PrivateKey loadPrivateKey(final String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final byte[] keyBytes = Base64.getDecoder().decode(keyString);
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(spec);
    }

    /**
     * Base64エンコードされたX.509形式の文字列から `PublicKey` オブジェクトを生成します。
     *
     * @param keyString デコードして処理する公開鍵の文字列。
     * @return 生成された `PublicKey` インスタンス。
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PublicKey loadPublicKey(final String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final byte[] keyBytes = Base64.getDecoder().decode(keyString);
        final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(spec);
    }

    /**
     * 指定されたパラメータを用いて、署名付きJWT（JWS）を生成する内部メソッドです。
     *
     * @param <T>              ペイロードの型
     * @param subject          トークンの主題。
     * @param payload          トークンに含めるカスタムクレーム。
     * @param expirationMillis トークンの有効期間（ミリ秒単位）。
     * @param privateKey       トークンの署名に使用する秘密鍵。
     * @return 生成された署名付きJWT文字列。
     */
    private <T> String generateToken(
        final String subject,
        final T payload,
        final long expirationMillis,
        final PrivateKey privateKey
    ) {
        final Map<String, Object> claims = objectMapper.convertValue(
            payload,
            new TypeReference<Map<String, Object>>() {}
        );

        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
            .issuer(this.issuer)
            .subject(subject)
            .claims(claims)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(privateKey)
            .compact();
    }

    /**
     * 指定された公開鍵を使用してJWTを検証し、そのクレームを抽出する内部メソッドです。
     *
     * @param token     検証対象のJWT。
     * @param publicKey トークンの署名検証に使用する公開鍵。
     * @return 検証後に抽出されたクレーム。
     */
    private Claims getClaims(final String token, final PublicKey publicKey) {
        final Jws<Claims> jws = Jwts.parser()
            .verifyWith(publicKey)
            .requireIssuer(this.issuer)
            .build()
            .parseSignedClaims(token);

        return jws.getPayload();
    }
}
