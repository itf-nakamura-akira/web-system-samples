package jp.co.itfllc.WebSystemSamples.advices;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jp.co.itfllc.WebSystemSamples.interceptors.AuthInterceptor;
import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TestController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
class GlobalExceptionHandlerTest {

    @MockitoBean
    private AuthInterceptor authInterceptor;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UsersMapper usersMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    @DisplayName("ResponseStatusExceptionを正しく処理できること")
    void handleResponseStatusException() throws Exception {
        mockMvc
            .perform(get("/response-status-exception"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Not Found"));
    }

    @Test
    @DisplayName("一般的なExceptionを正しく処理できること")
    void handleAllUncaughtException() throws Exception {
        mockMvc
            .perform(get("/exception"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.message").value("サーバーでエラーが発生しました。"));
    }
}
