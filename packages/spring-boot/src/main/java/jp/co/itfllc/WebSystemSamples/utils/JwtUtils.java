package jp.co.itfllc.WebSystemSamples.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWTユーティリティクラス
 * JWTの生成、検証、クレームの取得を行います。
 */
@Component
public class JwtUtils {

    /**
     * 秘密鍵文字列
     */
    @Value("${app.jwt.secret-private-key}")
    private String privateKeyString;

    /**
     * 公開鍵文字列
     */
    @Value("${app.jwt.secret-public-key}")
    private String publicKeyString;

    /**
     * JWTの発行者
     */
    @Value("${app.jwt.issuer}")
    private String issuer;

    /**
     * アクセストークンの有効期限（分）
     */
    @Value("${app.jwt.access-token-expire-minutes}")
    private long accessTokenExpireMinutes;

    /**
     * リフレッシュトークンの有効期限（日）
     */
    @Value("${app.jwt.jwt-refresh-token-expire-days}")
    private long refreshTokenExpireDays;

    /**
     * 秘密鍵
     */
    private PrivateKey privateKey;

    /**
     * 公開鍵
     */
    private PublicKey publicKey;

    /**
     * JSONオブジェクトマッパー
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 初期化メソッド
     * Base64エンコードされたキー文字列から、PrivateKeyとPublicKeyを生成します。
     */
    @PostConstruct
    public void init() {
        try {
            this.privateKey = this.loadPrivateKey(this.privateKeyString);
            this.publicKey = this.loadPublicKey(this.publicKeyString);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // アプリケーションの起動時にキーの読み込みに失敗した場合は、致命的なエラーとして処理します。
            throw new RuntimeException("Failed to load JWT keys", e);
        }
    }

    /**
     * 指定されたオブジェクトを基にアクセストークンを生成します。
     *
     * @param subject JWTの主題
     * @param payload クレームとして含めるオブジェクト
     * @return 生成されたJWT文字列
     */
    public <T> String generateAccessToken(String subject, T payload) {
        long expirationMillis = accessTokenExpireMinutes * 60 * 1000;

        return this.generateToken(subject, payload, expirationMillis);
    }

    /**
     * 指定されたオブジェクトを基にリフレッシュトークンを生成します。
     *
     * @param subject JWTの主題
     * @param payload クレームとして含めるオブジェクト
     * @return 生成されたリフレッシュトークン文字列
     */
    public <T> String generateRefreshToken(String subject, T payload) {
        long expirationMillis = refreshTokenExpireDays * 24 * 60 * 60 * 1000;

        return this.generateToken(subject, payload, expirationMillis);
    }

    /**
     * JWTを検証し、クレームを取得します。
     *
     * @param token 検証するJWT文字列
     * @return JWTのクレーム
     */
    public Claims getClaims(String token) {
        Jws<Claims> jws = Jwts.parser()
            .verifyWith(this.publicKey)
            .requireIssuer(this.issuer)
            .build()
            .parseSignedClaims(token);

        return jws.getPayload();
    }

    /**
     * Base64エンコードされたPKCS#8形式の秘密鍵文字列からPrivateKeyを生成します。
     *
     * @param keyString Base64エンコードされた秘密鍵文字列
     * @return PrivateKeyオブジェクト
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PrivateKey loadPrivateKey(String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(spec);
    }

    /**
     * Base64エンコードされたX.509形式の公開鍵文字列からPublicKeyを生成します。
     *
     * @param keyString Base64エンコードされた公開鍵文字列
     * @return PublicKeyオブジェクト
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PublicKey loadPublicKey(String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(spec);
    }

    /**
     * JWTを生成します。
     *
     * @param subject JWTの主題
     * @param payload クレームとして含めるオブジェクト
     * @param expirationMillis 有効期限（ミリ秒）
     * @return 生成されたJWT文字列
     */
    private <T> String generateToken(String subject, T payload, long expirationMillis) {
        Map<String, Object> claims = objectMapper.convertValue(payload, new TypeReference<Map<String, Object>>() {});

        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
            .issuer(this.issuer)
            .subject(subject)
            .claims(claims)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(this.privateKey)
            .compact();
    }
}
