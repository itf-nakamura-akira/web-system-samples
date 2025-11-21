package jp.co.itfllc.WebSystemSamples.utils;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * 暗号化 Util
 */
public class CryptoUtils {

    /**
     * Argon2 エンコーダー
     * Spring Security 5.8のデフォルト設定を利用します。
     */
    private static final Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    /**
     * パスワードをハッシュ化します。
     *
     * @param password ハッシュ化するパスワード
     * @return ハッシュ化されたパスワード
     */
    public static String hashPassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("パスワードが指定されていません。");
        }

        return encoder.encode(password);
    }

    /**
     * パスワードがハッシュと一致するかどうかを検証します。
     *
     * @param hash     検証対象のハッシュ化されたパスワード
     * @param password 検証する平文のパスワード
     * @return パスワードが一致する場合は true、それ以外は false
     */
    public static boolean verifyPassword(String hash, String password) {
        if (!StringUtils.hasText(hash)) {
            throw new IllegalArgumentException("ハッシュ化されたパスワードが指定されていません。");
        }

        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("検証するパスワードが指定されていません。");
        }

        return encoder.matches(password, hash);
    }
}
