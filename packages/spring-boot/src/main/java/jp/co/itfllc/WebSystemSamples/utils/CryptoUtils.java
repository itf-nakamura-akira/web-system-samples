package jp.co.itfllc.WebSystemSamples.utils;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * パスワードのハッシュ化や検証など、暗号化関連の操作を提供するユーティリティクラスです。
 * このクラスはインスタンス化できず、静的メソッドのみを提供します。
 */
public class CryptoUtils {

    /**
     * パスワードのハッシュ化に使用する {@code Argon2PasswordEncoder} のインスタンスです。
     * Spring Security 5.8 の推奨するデフォルト設定で初期化されています。
     */
    private static final Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    /**
     * このクラスはユーティリティクラスであり、インスタンス化は意図されていません。
     */
    private CryptoUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 指定された平文のパスワードをArgon2アルゴリズムを用いてハッシュ化します。
     *
     * @param password ハッシュ化対象の平文パスワード。
     * @return Argon2でハッシュ化されたパスワード文字列。
     * @throws IllegalArgumentException パスワードがnullまたは空文字列の場合。
     */
    public static String hashPassword(final String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("パスワードが指定されていません。");
        }

        return encoder.encode(password);
    }

    /**
     * 指定された平文のパスワードが、既存のハッシュ値と一致するかどうかを検証します。
     *
     * @param hash     比較対象となるArgon2でハッシュ化されたパスワード文字列。
     * @param password 検証対象の平文パスワード。
     * @return パスワードが一致する場合は {@code true}、一致しない場合は {@code false}。
     * @throws IllegalArgumentException ハッシュまたはパスワードがnullまたは空文字列の場合。
     */
    public static boolean verifyPassword(final String hash, final String password) {
        if (!StringUtils.hasText(hash)) {
            throw new IllegalArgumentException("ハッシュ化されたパスワードが指定されていません。");
        }

        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("検証するパスワードが指定されていません。");
        }

        return encoder.matches(password, hash);
    }
}
