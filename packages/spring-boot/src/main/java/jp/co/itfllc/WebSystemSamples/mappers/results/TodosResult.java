package jp.co.itfllc.WebSystemSamples.mappers.results;

import jp.co.itfllc.WebSystemSamples.mappers.results.entities.TodosEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TodosResult extends TodosEntity {

    /**
     * 作成者
     */
    private String creator;

    /**
     * 担当者
     */
    private String assignee;
}
