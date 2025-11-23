package jp.co.itfllc.WebSystemSamples.mappers.results;

import jp.co.itfllc.WebSystemSamples.mappers.results.entities.TodosEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO情報の取得結果を表すクラスです。
 * {@link TodosEntity} を継承し、作成者名と担当者名のフィールドを追加しています。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TodosResult extends TodosEntity {

    /**
     * TODOを作成したユーザーの名前。
     */
    private String creator;

    /**
     * TODOを担当するユーザーの名前。
     */
    private String assignee;
}
