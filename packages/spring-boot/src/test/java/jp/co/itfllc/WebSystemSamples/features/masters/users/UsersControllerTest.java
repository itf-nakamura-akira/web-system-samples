package jp.co.itfllc.WebSystemSamples.features.masters.users;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import jp.co.itfllc.WebSystemSamples.enums.Role;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * UsersController の単体テストクラスです。
 */
@WebMvcTest(UsersController.class)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsersService usersService;

    @Test
    @DisplayName("ユーザーリストが正しく取得できること")
    public void testGetList() throws Exception {
        // GIVEN: ユーザーデータが2件存在する場合
        List<UsersEntity> expectedUsers = new ArrayList<>();
        UsersEntity user1 = new UsersEntity();
        user1.setId("user1");
        user1.setAccount("testuser1");
        user1.setName("テストユーザー1");
        user1.setDisabledAt(null);
        user1.setRole(Role.Common);
        expectedUsers.add(user1);

        UsersEntity user2 = new UsersEntity();
        user2.setId("user2");
        user2.setAccount("testuser2");
        user2.setName("テストユーザー2");
        user2.setDisabledAt(OffsetDateTime.now());
        user2.setRole(Role.Admin);
        expectedUsers.add(user2);

        when(usersService.getList()).thenReturn(expectedUsers);

        // WHEN: ユーザーリスト取得APIを呼び出すと
        // THEN: 200 OKが返却され、2件のユーザー情報が返却されること
        mockMvc
            .perform(get("/masters/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.users.length()").value(2))
            .andExpect(jsonPath("$.users[0].id").value("user1"))
            .andExpect(jsonPath("$.users[0].account").value("testuser1"))
            .andExpect(jsonPath("$.users[0].name").value("テストユーザー1"))
            .andExpect(jsonPath("$.users[0].disabledAt").isEmpty())
            .andExpect(jsonPath("$.users[0].role").value("Common"))
            .andExpect(jsonPath("$.users[1].id").value("user2"))
            .andExpect(jsonPath("$.users[1].account").value("testuser2"))
            .andExpect(jsonPath("$.users[1].name").value("テストユーザー2"))
            .andExpect(jsonPath("$.users[1].disabledAt").isNotEmpty())
            .andExpect(jsonPath("$.users[1].role").value("Admin"));
    }
}
