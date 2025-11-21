package jp.co.itfllc.WebSystemSamples.mappers.Todos.models;

import java.util.List;

import jp.co.itfllc.WebSystemSamples.entities.Todos;
import jp.co.itfllc.WebSystemSamples.entities.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TodosWithUsers extends Users {
    /**
     * Todo リスト
     */
    private List<Todos> todos;
}
