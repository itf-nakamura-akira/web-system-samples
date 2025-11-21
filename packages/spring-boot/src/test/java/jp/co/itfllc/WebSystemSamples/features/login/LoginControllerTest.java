package jp.co.itfllc.WebSystemSamples.features.login;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

/**
 * LoginController のテストクラスです。
 */
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoginService loginService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("正常系: 認証が成功した場合、アクセストークンとリフレッシュトークンが返されること")
    void testPostLogin_success() throws Exception {
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

        // WHEN & THEN
        mockMvc
            .perform(
                post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("dummy-access-token"))
            .andExpect(jsonPath("$.refreshToken").value("dummy-refresh-token"));
    }

    @Test
    @DisplayName("異常系: 認証が失敗した場合、401 Unauthorizedが返されること")
    void testPostLogin_failure() throws Exception {
        // GIVEN
        // ログインリクエストの準備
        PostLoginRequest request = new PostLoginRequest("test.user", "wrong-password");
        // サービスが認証失敗の例外をスローするように設定
        when(loginService.login("test.user", "wrong-password")).thenThrow(
            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "アカウントまたはパスワードが誤っています。")
        );

        // WHEN & THEN
        mockMvc
            .perform(
                post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isUnauthorized());
    }
}
