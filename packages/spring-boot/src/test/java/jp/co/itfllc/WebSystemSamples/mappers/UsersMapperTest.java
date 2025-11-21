package jp.co.itfllc.WebSystemSamples.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
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
            Optional<UsersEntity> userOptional = usersMapper.selectByAccount(account);

            // THEN
            assertThat(userOptional).isPresent();
            UsersEntity user = userOptional.get();
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
            Optional<UsersEntity> user = usersMapper.selectByAccount(account);

            // THEN
            assertThat(user).isNotPresent();
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

    @Nested
    @DisplayName("selectListのテスト")
    class SelectList {

        @Test
        @DisplayName("ユーザーリストが全件取得できること")
        void testSelectList_success() {
            // WHEN
            // テスト対象のメソッドを実行します
            List<UsersEntity> users = usersMapper.selectList();

            // THEN
            // 結果が期待通りであることを検証します
            assertThat(users).isNotNull().isNotEmpty();
            // 少なくとも6,000件以上のユーザーが取得できることを確認します
            assertThat(users.size()).isGreaterThan(6000);
            // 取得したリストの最初のユーザーの必須項目がnullでないことを確認します
            assertThat(users.get(0)).isNotNull();
            assertThat(users.get(0).getAccount()).isEqualTo("nakamura.akira");
            assertThat(users.get(0).getHashedPassword()).isNotEmpty();
            assertThat(users.get(0).getDisabledAt()).isNull();
            assertThat(users.get(0).getRole()).isEqualTo(Role.Admin);
            assertThat(users.get(0).getName()).isEqualTo("中村輝");
        }
    }

    @Nested
    @DisplayName("selectByIdのテスト")
    class SelectById {

        @Test
        @DisplayName("指定したIDが存在する場合、ユーザー情報が取得できること")
        void testSelectById_existingUser() {
            // GIVEN
            // 既存のユーザー情報を取得
            UsersEntity expectedUser = usersMapper.selectByAccount("nakamura.akira").get();
            String userId = expectedUser.getId();

            // WHEN
            // IDでユーザー情報を取得
            Optional<UsersEntity> actualUserOptional = usersMapper.selectById(userId);

            // THEN
            // 取得したユーザー情報が期待通りであることを検証
            assertThat(actualUserOptional).isPresent();
            UsersEntity actualUser = actualUserOptional.get();
            assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
            assertThat(actualUser.getAccount()).isEqualTo(expectedUser.getAccount());
            assertThat(actualUser.getHashedPassword()).isEqualTo(expectedUser.getHashedPassword());
            assertThat(actualUser.getName()).isEqualTo(expectedUser.getName());
            assertThat(actualUser.getDisabledAt()).isEqualTo(expectedUser.getDisabledAt());
            assertThat(actualUser.getRole()).isEqualTo(expectedUser.getRole());
        }

        @Test
        @DisplayName("指定したIDが存在しない場合、nullが返却されること")
        void testSelectById_nonExistingUser() {
            // GIVEN
            // 存在しないUUIDを生成
            String nonExistingId = "00000000-0000-0000-0000-000000000000";

            // WHEN
            // 存在しないIDでユーザー情報を取得
            Optional<UsersEntity> user = usersMapper.selectById(nonExistingId);

            // THEN
            // 結果がnullであることを検証
            assertThat(user).isNotPresent();
        }
    }
}
