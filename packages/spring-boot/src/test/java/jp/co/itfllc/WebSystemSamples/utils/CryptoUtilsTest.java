package jp.co.itfllc.WebSystemSamples.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CryptoUtilsTest {

    @Nested
    @DisplayName("hashPassword メソッドのテスト")
    class HashPasswordTests {

        @Test
        @DisplayName("有効なパスワードをハッシュ化できる")
        void testHashPassword_withValidPassword() {
            // GIVEN
            String rawPassword = "password123";

            // WHEN
            String hashedPassword = CryptoUtils.hashPassword(rawPassword);

            // THEN
            assertNotNull(hashedPassword, "ハッシュ化されたパスワードは null であってはなりません。");
            assertNotEquals(
                rawPassword,
                hashedPassword,
                "ハッシュ化されたパスワードは元のパスワードと異なるべきです。"
            );
            assertTrue(
                CryptoUtils.verifyPassword(hashedPassword, rawPassword),
                "生成されたハッシュは元のパスワードで検証できるべきです。"
            );
        }

        @Test
        @DisplayName("null のパスワードで IllegalArgumentException をスローする")
        void testHashPassword_withNullPassword() {
            // GIVEN
            String rawPassword = null;

            // WHEN & THEN
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    CryptoUtils.hashPassword(rawPassword);
                },
                "null のパスワードは IllegalArgumentException をスローすべきです。"
            );
            assertEquals("パスワードが指定されていません。", exception.getMessage());
        }

        @Test
        @DisplayName("空のパスワードで IllegalArgumentException をスローする")
        void testHashPassword_withEmptyPassword() {
            // GIVEN
            String rawPassword = "";

            // WHEN & THEN
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    CryptoUtils.hashPassword(rawPassword);
                },
                "空のパスワードは IllegalArgumentException をスローすべきです。"
            );
            assertEquals("パスワードが指定されていません。", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("verifyPassword メソッドのテスト")
    class VerifyPasswordTests {

        private String rawPassword = "password123";
        private String hashedPassword;

        @BeforeEach
        void setUp() {
            hashedPassword = CryptoUtils.hashPassword(rawPassword);
        }

        @Test
        @DisplayName("正しいパスワードで true を返す")
        void testVerifyPassword_withCorrectPassword() {
            assertTrue(
                CryptoUtils.verifyPassword(hashedPassword, rawPassword),
                "正しいパスワードで検証した場合、true を返すべきです。"
            );
        }

        @Test
        @DisplayName("間違ったパスワードで false を返す")
        void testVerifyPassword_withIncorrectPassword() {
            assertFalse(
                CryptoUtils.verifyPassword(hashedPassword, "wrongpassword"),
                "間違ったパスワードで検証した場合、false を返すべきです。"
            );
        }

        @Test
        @DisplayName("null のハッシュで IllegalArgumentException をスローする")
        void testVerifyPassword_withNullHash() {
            // GIVEN
            String hash = null;

            // WHEN & THEN
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CryptoUtils.verifyPassword(hash, rawPassword)
            );
            assertEquals("ハッシュ化されたパスワードが指定されていません。", exception.getMessage());
        }

        @Test
        @DisplayName("空のハッシュで IllegalArgumentException をスローする")
        void testVerifyPassword_withEmptyHash() {
            // GIVEN
            String hash = "";

            // WHEN & THEN
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CryptoUtils.verifyPassword(hash, rawPassword)
            );
            assertEquals("ハッシュ化されたパスワードが指定されていません。", exception.getMessage());
        }

        @Test
        @DisplayName("空白のハッシュで IllegalArgumentException をスローする")
        void testVerifyPassword_withBlankHash() {
            // GIVEN
            String hash = " ";

            // WHEN & THEN
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CryptoUtils.verifyPassword(hash, rawPassword)
            );
            assertEquals("ハッシュ化されたパスワードが指定されていません。", exception.getMessage());
        }

        @Test
        @DisplayName("null のパスワードで IllegalArgumentException をスローする")
        void testVerifyPassword_withNullPassword() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                CryptoUtils.verifyPassword(hashedPassword, null);
            });
            assertEquals("検証するパスワードが指定されていません。", exception.getMessage());
        }

        @Test
        @DisplayName("空のパスワードで IllegalArgumentException をスローする")
        void testVerifyPassword_withEmptyPassword() {
            // GIVEN
            String password = "";

            // WHEN & THEN
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                CryptoUtils.verifyPassword(hashedPassword, password);
            });
            assertEquals("検証するパスワードが指定されていません。", exception.getMessage());
        }

        @Test
        @DisplayName("空白のパスワードで IllegalArgumentException をスローする")
        void testVerifyPassword_withBlankPassword() {
            // GIVEN
            String password = " ";

            // WHEN & THEN
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                CryptoUtils.verifyPassword(hashedPassword, password);
            });
            assertEquals("検証するパスワードが指定されていません。", exception.getMessage());
        }
    }
}
