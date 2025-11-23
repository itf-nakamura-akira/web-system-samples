package jp.co.itfllc.WebSystemSamples.mappers.results.entities;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Data;

/**
 * `todos` テーブルのレコードを表すエンティティクラスです。
 */
@Data
public class TodosEntity {

    /**
     * 主キーとなるTODOのID。
     */
    private String id;

    /**
     * このTODO項目を作成したユーザーのID。
     */
    private String creatorId;

    /**
     * このTODO項目の担当者のユーザーID。
     */
    private String assigneeId;

    /**
     * TODOの内容を簡潔に表すタイトル。
     */
    private String title;

    /**
     * TODOに関する詳細な情報やメモ。
     */
    private String memo;

    /**
     * TODOの完了期限日。
     */
    private LocalDate dueDate;

    /**
     * TODOが完了した日時。未完了の場合は {@code null}。
     */
    private OffsetDateTime completedAt;

    /**
     * このレコードが作成された日時。
     */
    private OffsetDateTime createdAt;

    /**
     * このレコードが最後に更新された日時。
     */
    private OffsetDateTime updatedAt;
}
