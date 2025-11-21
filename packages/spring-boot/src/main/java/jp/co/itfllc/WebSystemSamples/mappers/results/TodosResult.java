package jp.co.itfllc.WebSystemSamples.mappers.results;

import jp.co.itfllc.WebSystemSamples.mappers.results.entities.TodosEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TodosResult extends TodosEntity {
    // TODO 担当者の表示名とか取得する
}
