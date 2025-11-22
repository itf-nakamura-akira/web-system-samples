package jp.co.itfllc.WebSystemSamples.mappers.results.entities;

import java.time.OffsetDateTime;
import lombok.Data;

/**
 * JWT(リフレッシュトークン)を管理するテーブル
 */
@Data
public class RefreshTokensEntity {

    /**
     * ID
     */
    private String id;

    /**
     * ユーザーテーブルID
     */
    private String usersId;

    /**
     * ハッシュ化済みリフレッシュトークン
     */
    private String hashedToken;

    /**
     * 発行日時
     */
    private OffsetDateTime createdAt;

    /**
     * 失効期限
     */
    private OffsetDateTime expiresAt;

    /**
     * 失効フラグ
     */
    private Boolean revoked;
}
