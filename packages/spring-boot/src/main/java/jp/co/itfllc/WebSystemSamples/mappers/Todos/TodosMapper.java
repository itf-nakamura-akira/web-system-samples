package jp.co.itfllc.WebSystemSamples.mappers.Todos;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import jp.co.itfllc.WebSystemSamples.mappers.Todos.models.TodosWithUsers;

/**
 * TODOテーブル向け Mapper
 */
@Mapper
public interface TodosMapper {
    @Select("""
        SELECT
            u.*,
            t.*
        FROM
            users u
        INNER JOIN
            todos t
        ON
            u.id = t.assignee_id
        WHERE
            u.id = #{assigneeId};
    """)
    TodosWithUsers selectByAssigneeId(String assigneeId);
}
