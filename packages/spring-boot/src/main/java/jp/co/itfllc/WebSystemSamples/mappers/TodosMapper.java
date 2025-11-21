package jp.co.itfllc.WebSystemSamples.mappers;

import java.util.List;
import jp.co.itfllc.WebSystemSamples.mappers.results.TodosResult;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODOテーブル向け Mapper
 */
@Mapper
public interface TodosMapper {
    /**
     * 担当者のユーザーIDに紐づくTODO一覧を取得する
     *
     * @param assigneeId 担当者のユーザーID
     * @return TODOレコード
     */
    List<TodosResult> selectByAssigneeId(String assigneeId);
}
