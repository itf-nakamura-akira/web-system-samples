package jp.co.itfllc.WebSystemSamples.features.masters.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * UsersService の単体テストクラスです。
 */
public class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersMapper usersMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("ユーザーリストが正しく取得できること")
    public void testGetList() {
        // GIVEN: ユーザーデータが2件存在する場合
        List<UsersEntity> expectedUsers = new ArrayList<>();
        UsersEntity user1 = new UsersEntity();
        user1.setId("user1");
        user1.setName("テストユーザー1");
        expectedUsers.add(user1);

        UsersEntity user2 = new UsersEntity();
        user2.setId("user2");
        user2.setName("テストユーザー2");
        expectedUsers.add(user2);

        when(usersMapper.selectList()).thenReturn(expectedUsers);

        // WHEN: ユーザーリスト取得処理を呼び出すと
        List<UsersEntity> actualUsers = usersService.getList();

        // THEN: 2件のユーザー情報が返却されること
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers.get(0).getId(), actualUsers.get(0).getId());
        assertEquals(expectedUsers.get(0).getName(), actualUsers.get(0).getName());
        assertEquals(expectedUsers.get(1).getId(), actualUsers.get(1).getId());
        assertEquals(expectedUsers.get(1).getName(), actualUsers.get(1).getName());
    }
}
