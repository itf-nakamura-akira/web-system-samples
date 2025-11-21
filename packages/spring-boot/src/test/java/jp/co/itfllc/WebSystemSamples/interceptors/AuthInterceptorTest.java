package jp.co.itfllc.WebSystemSamples.interceptors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;
import java.util.Optional;
import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UsersMapper usersMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @InjectMocks
    private AuthInterceptor authInterceptor;

    @Test
    @DisplayName("Authorizationヘッダーがない場合、ResponseStatusExceptionをスローすること")
    void preHandle_noAuthHeader_throwsResponseStatusException() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            authInterceptor.preHandle(request, response, handler);
        });
    }

    @Test
    @DisplayName("AuthorizationヘッダーがBearerでない場合、ResponseStatusExceptionをスローすること")
    void preHandle_notBearerToken_throwsResponseStatusException() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Basic some-token");

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            authInterceptor.preHandle(request, response, handler);
        });
    }

    @Test
    @DisplayName("無効なJWTの場合、ResponseStatusExceptionをスローすること")
    void preHandle_invalidJwt_throwsResponseStatusException() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtUtils.getClaims("invalid-token")).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            authInterceptor.preHandle(request, response, handler);
        });
    }

    @Test
    @DisplayName("ユーザーが存在しない場合、ResponseStatusExceptionをスローすること")
    void preHandle_userNotFound_throwsResponseStatusException() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        Claims claims = Jwts.claims().subject("testuser").build();
        when(jwtUtils.getClaims("valid-token")).thenReturn(claims);
        when(usersMapper.selectByAccount("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            authInterceptor.preHandle(request, response, handler);
        });
    }

    @Test
    @DisplayName("ユーザーが無効化されている場合、ResponseStatusExceptionをスローすること")
    void preHandle_userDisabled_throwsResponseStatusException() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        Claims claims = Jwts.claims().subject("testuser").build();
        when(jwtUtils.getClaims("valid-token")).thenReturn(claims);
        UsersEntity user = new UsersEntity();
        user.setDisabledAt(OffsetDateTime.now());
        when(usersMapper.selectByAccount("testuser")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            authInterceptor.preHandle(request, response, handler);
        });
    }

    @Test
    @DisplayName("有効なJWTでユーザーも有効な場合、trueを返し、リクエスト属性にユーザー情報を設定すること")
    void preHandle_validJwtAndUser_returnsTrue() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        Claims claims = Jwts.claims().subject("testuser").build();
        when(jwtUtils.getClaims("valid-token")).thenReturn(claims);
        UsersEntity user = new UsersEntity();
        when(usersMapper.selectByAccount("testuser")).thenReturn(Optional.of(user));

        // Act
        boolean result = authInterceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).setAttribute("user", user);
    }
}
