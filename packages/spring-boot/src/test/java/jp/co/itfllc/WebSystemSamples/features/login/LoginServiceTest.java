package jp.co.itfllc.WebSystemSamples.features.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Optional;
import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.CryptoUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * LoginService の単体テストクラスです。
 */
@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UsersMapper usersMapper;

    @InjectMocks
    private LoginService loginService;

    private MockedStatic<CryptoUtils> cryptoUtilsMock;

    @BeforeEach
    void setUp() {
        // CryptoUtilsの静的メソッドをモックします
        cryptoUtilsMock = mockStatic(CryptoUtils.class);
    }

    @AfterEach
    void tearDown() {
        // 各テストの後にモックをクローズします
        cryptoUtilsMock.close();
    }

    @Test
    @DisplayName("ログイン成功のテスト")
    void testLogin_Success() {
        // GIVEN: ユーザーが存在し、パスワードが正しい場合
        UsersEntity dummyUser = new UsersEntity();
        dummyUser.setAccount("testuser");
        dummyUser.setHashedPassword("hashed_password");

        when(usersMapper.selectByAccount("testuser")).thenReturn(Optional.of(dummyUser));
        cryptoUtilsMock.when(() -> CryptoUtils.verifyPassword("hashed_password", "password123")).thenReturn(true);

        // WHEN: ログイン処理を呼び出すと
        UsersEntity result = loginService.login("testuser", "password123");

        // THEN: ユーザー情報が返却されること
        assertNotNull(result);
        assertEquals("testuser", result.getAccount());
    }

    @Test
    @DisplayName("ログイン失敗のテスト（ユーザーが存在しない）")
    void testLogin_Failure_UserNotFound() {
        // GIVEN: ユーザーが存在しない場合
        when(usersMapper.selectByAccount(anyString())).thenReturn(Optional.empty());

        // WHEN: ログイン処理を呼び出すと
        // THEN: ResponseStatusExceptionがスローされること
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            loginService.login("unknownuser", "password123");
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("アカウントまたはパスワードが誤っています。", exception.getReason());
    }

    @Test
    @DisplayName("ログイン失敗のテスト（パスワードが不正）")
    void testLogin_Failure_IncorrectPassword() {
        // GIVEN: ユーザーは存在するが、パスワードが正しくない場合
        UsersEntity dummyUser = new UsersEntity();
        dummyUser.setAccount("testuser");
        dummyUser.setHashedPassword("hashed_password");

        when(usersMapper.selectByAccount("testuser")).thenReturn(Optional.of(dummyUser));
        cryptoUtilsMock.when(() -> CryptoUtils.verifyPassword("hashed_password", "wrongpassword")).thenReturn(false);

        // WHEN: ログイン処理を呼び出すと
        // THEN: ResponseStatusExceptionがスローされること
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            loginService.login("testuser", "wrongpassword");
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("アカウントまたはパスワードが誤っています。", exception.getReason());
    }
}
