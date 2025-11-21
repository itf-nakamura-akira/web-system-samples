package jp.co.itfllc.WebSystemSamples.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import jp.co.itfllc.WebSystemSamples.TestHelper;
import jp.co.itfllc.WebSystemSamples.mappers.results.TodosResult;
import jp.co.itfllc.WebSystemSamples.mappers.results.UsersResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@MybatisTest
@Import(TestHelper.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "file:/workspace/.devcontainer/app/.env")
class TodosMapperTest {

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private TodosMapper todosMapper;

    @Nested
    @DisplayName("selectByAssigneeId メソッドのテスト")
    class SelectByAssigneeId {

        @Test
        @DisplayName("存在する担当者IDを指定した場合、紐づくTODOリストが返されること")
        void testSelectByAssigneeId_whenAssigneeExists() {
            // GIVEN
            UsersResult assigneeUser = testHelper.getUserByAccount("nakamura.akira");

            // WHEN
            List<TodosResult> actual = todosMapper.selectByAssigneeId(assigneeUser.getId());

            // THEN
            assertThat(actual).hasSize(8);
            assertThat(actual.get(0).getAssigneeId()).isEqualTo(assigneeUser.getId());
            assertThat(actual.get(0).getTitle()).isNotNull();
        }

        @Test
        @DisplayName("存在しない担当者IDを指定した場合、空のリストが返されること")
        void testSelectByAssigneeId_whenAssigneeDoesNotExist() {
            // GIVEN
            String assigneeId = "00000000-0000-0000-0000-000000000000";

            // WHEN
            List<TodosResult> actual = todosMapper.selectByAssigneeId(assigneeId);

            // THEN
            assertThat(actual).isNotNull().isEmpty();
        }
    }
}
