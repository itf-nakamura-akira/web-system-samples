package jp.co.itfllc.WebSystemSamples;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jp.co.itfllc.WebSystemSamples.advices.GlobalExceptionHandler;
import jp.co.itfllc.WebSystemSamples.interceptors.AuthInterceptor;
import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(
    controllers = WebMvcConfig.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class),
    }
)
class WebMvcConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private AuthInterceptor authInterceptor;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UsersMapper usersMapper;

    @BeforeEach
    void setUp() throws Exception {
        // MockMvcを初期化します
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        // 認証インターセプターが常に例外を返すように設定
        when(authInterceptor.preHandle(any(), any(), any())).thenThrow(
            new ResponseStatusException(HttpStatus.UNAUTHORIZED)
        );
    }

    @Test
    @DisplayName("インターセプターの対象外パスにアクセスできること")
    void testAddInterceptors_ExcludePath() throws Exception {
        // "/login" は除外されているので、インターセプターは呼ばれるが、後続の処理に進む
        // マッピングされるコントローラーがないため、404エラーが返される
        mockMvc.perform(get("/login")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("インターセプターの対象パスにアクセスできること")
    void testAddInterceptors_IncludePath() throws Exception {
        // "/some-other-path" はインターセプターの対象なので、インターセプターが呼ばれる
        // preHandleが例外を返す
        mockMvc.perform(get("/some-other-path")).andExpect(status().isUnauthorized());
    }
}
