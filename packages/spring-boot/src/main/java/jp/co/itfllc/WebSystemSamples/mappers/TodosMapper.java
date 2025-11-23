package jp.co.itfllc.WebSystemSamples.mappers;

import java.util.List;
import jp.co.itfllc.WebSystemSamples.mappers.results.TodosResult;
import org.apache.ibatis.annotations.Mapper;

/**
 * `todos` テーブルへのデータアクセスを提供するMyBatisのマッパーインターフェースです。
 */
@Mapper
public interface TodosMapper {
    /**
     * 指定された担当者のユーザーIDに紐づく全てのTODO項目を取得します。
     *
     * @param assigneeId 検索対象の担当者（ユーザー）のID。
     * @return 検索結果のTODOリスト。見つからない場合は空のリスト。
     */
    List<TodosResult> selectByAssigneeId(final String assigneeId);
}
