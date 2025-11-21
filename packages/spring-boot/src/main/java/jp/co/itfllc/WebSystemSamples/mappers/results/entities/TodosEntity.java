package jp.co.itfllc.WebSystemSamples.mappers.results.entities;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Data;

/**
 * TODOテーブル
 */
@Data
public class TodosEntity {

    /**
     * ID
     */
    private String id;

    /**
     * 作成者ID
     */
    private String creatorId;

    /**
     * 担当者ID
     */
    private String assigneeId;

    /**
     * TODOのタイトル
     */
    private String title;

    /**
     * メモ
     */
    private String memo;

    /**
     * 期限日
     */
    private LocalDate dueDate;

    /**
     * 完了日時
     */
    private OffsetDateTime completedAt;

    /**
     * 作成日時
     */
    private OffsetDateTime createdAt;

    /**
     * 更新日時
     */
    private OffsetDateTime updatedAt;
}
