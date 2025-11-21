package jp.co.itfllc.WebSystemSamples.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import jp.co.itfllc.WebSystemSamples.enums.Role;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "file:/workspace/.devcontainer/app/.env")
class UsersMapperTest {

    @Autowired
    private UsersMapper usersMapper;

    @Nested
    @DisplayName("selectByAccountのテスト")
    class SelectByAccount {

        @Test
        @DisplayName("指定したアカウントが存在する場合、ユーザー情報が取得できること")
        void testSelectByAccount_existingUser() {
            // GIVEN
            String account = "nakamura.akira";

            // WHEN
            UsersEntity user = usersMapper.selectByAccount(account);

            // THEN
            assertThat(user).isNotNull();
            assertThat(user.getAccount()).isEqualTo(account);
            assertThat(user.getHashedPassword()).isNotEmpty();
            assertThat(user.getDisabledAt()).isNull();
            assertThat(user.getRole()).isEqualTo(Role.Admin);
            assertThat(user.getName()).isEqualTo("中村輝");
        }

        @Test
        @DisplayName("指定したアカウントが存在しない場合、nullが返却されること")
        void testSelectByAccount_nonExistingUser() {
            // GIVEN
            String account = "nonexistent";

            // WHEN
            UsersEntity user = usersMapper.selectByAccount(account);

            // THEN
            assertThat(user).isNull();
        }
    }

    @Nested
    @DisplayName("insertのテスト")
    class Insert {

        @Test
        @DisplayName("新しいユーザーがDBに保存できること")
        void testInsert_newUser() {
            // GIVEN
            UsersEntity newUser = new UsersEntity();
            newUser.setAccount("test.user");
            newUser.setHashedPassword("test_password_hashed");
            newUser.setName("テストユーザー");
            newUser.setDisabledAt(null);
            newUser.setRole(Role.Admin);

            // WHEN
            UsersEntity insertedUser = usersMapper.insert(newUser);

            // THEN
            assertThat(insertedUser).isNotNull();
            assertThat(insertedUser.getId()).isNotNull();
            assertThat(insertedUser.getAccount()).isEqualTo(newUser.getAccount());
            assertThat(insertedUser.getHashedPassword()).isEqualTo(newUser.getHashedPassword());
            assertThat(insertedUser.getName()).isEqualTo(newUser.getName());
            assertThat(insertedUser.getDisabledAt()).isNull();
            assertThat(insertedUser.getRole()).isEqualTo(newUser.getRole());
        }
    }
}
