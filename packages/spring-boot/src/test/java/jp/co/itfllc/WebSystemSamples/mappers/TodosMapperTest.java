package jp.co.itfllc.WebSystemSamples.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import jp.co.itfllc.WebSystemSamples.mappers.results.TodosResult;
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
class TodosMapperTest {

    @Autowired
    private TodosMapper todosMapper;

    @Nested
    @DisplayName("selectByAssigneeId メソッドのテスト")
    class SelectByAssigneeId {

        @Test
        @DisplayName("存在する担当者IDを指定した場合、紐づくTODOリストが返されること")
        void testSelectByAssigneeId_whenAssigneeExists() {
            // GIVEN
            String assigneeId = "019a0087-25f2-7322-8953-984b71cebff4";

            // WHEN
            List<TodosResult> actual = todosMapper.selectByAssigneeId(assigneeId);

            // THEN
            // 8件のTODOが取得できるはず
            assertThat(actual).hasSize(8);
            // 取得したTODOの内容を検証
            assertThat(actual.get(0).getAssigneeId()).isEqualTo(assigneeId);
            assertThat(actual.get(0).getTitle()).isNotNull();
        }

        @Test
        @DisplayName("存在しない担当者IDを指定した場合、空のリストが返されること")
        void testSelectByAssigneeId_whenAssigneeDoesNotExist() {
            // GIVEN
            String assigneeId = "019a09cb-e304-76ae-b48f-69d94e9ee30a";

            // WHEN
            List<TodosResult> actual = todosMapper.selectByAssigneeId(assigneeId);

            // THEN
            assertThat(actual).isNotNull().isEmpty();
        }
    }
}
