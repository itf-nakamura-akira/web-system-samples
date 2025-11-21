package jp.co.itfllc.WebSystemSamples.features.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import jp.co.itfllc.WebSystemSamples.features.login.models.PostLoginRequest;
import jp.co.itfllc.WebSystemSamples.features.login.models.PostLoginResponse;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * LoginController のテストクラスです。
 */
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginService loginService;

    @Mock
    private JwtUtils jwtUtils;

    @Nested
    @DisplayName("postLoginメソッドのテスト")
    class PostLogin {

        @Test
        @DisplayName("正常系: 認証が成功した場合、アクセストークンとリフレッシュトークンが返されること")
        void testPostLogin_success() {
            // GIVEN
            // ログインリクエストの準備
            PostLoginRequest request = new PostLoginRequest("test.user", "password");
            // ログイン成功時に返されるユーザーエンティティの準備
            UsersEntity loginUser = new UsersEntity();
            loginUser.setAccount("test.user");
            // サービスが成功レスポンスを返すように設定
            when(loginService.login("test.user", "password")).thenReturn(loginUser);
            // JWTユーティリティがダミーのトークンを返すように設定
            when(jwtUtils.generateAccessToken(anyString(), anyMap())).thenReturn("dummy-access-token");
            when(jwtUtils.generateRefreshToken(anyString(), anyMap())).thenReturn("dummy-refresh-token");

            // WHEN
            // テスト対象メソッドの実行
            PostLoginResponse response = loginController.postLogin(request);

            // THEN
            // レスポンスが期待通りであることを検証
            assertThat(response).isNotNull();
            assertThat(response.accessToken()).isEqualTo("dummy-access-token");
            assertThat(response.refreshToken()).isEqualTo("dummy-refresh-token");

            // 依存メソッドが正しく呼び出されたことを検証
            verify(loginService).login("test.user", "password");
            verify(jwtUtils).generateAccessToken(loginUser.getAccount(), new HashMap<>());
            verify(jwtUtils).generateRefreshToken(loginUser.getAccount(), new HashMap<>());
        }

        @Test
        @DisplayName("異常系: 認証が失敗した場合、BadCredentialsExceptionがスローされること")
        void testPostLogin_failure() {
            // GIVEN
            // ログインリクエストの準備
            PostLoginRequest request = new PostLoginRequest("test.user", "wrong-password");
            // サービスが認証失敗の例外をスローするように設定
            when(loginService.login("test.user", "wrong-password")).thenThrow(
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "アカウントまたはパスワードが誤っています。")
            );

            // WHEN & THEN
            // 例外がスローされることを検証
            assertThrows(ResponseStatusException.class, () -> {
                loginController.postLogin(request);
            });

            // 依存メソッドが呼び出されたことを検証
            verify(loginService).login("test.user", "wrong-password");
            // トークン生成が呼び出されないことを検証
            verify(jwtUtils, never()).generateAccessToken(any(), any());
            verify(jwtUtils, never()).generateRefreshToken(any(), any());
        }
    }
}
