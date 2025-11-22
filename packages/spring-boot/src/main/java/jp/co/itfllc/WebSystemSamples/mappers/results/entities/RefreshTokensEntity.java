package jp.co.itfllc.WebSystemSamples.mappers.results.entities;

import java.time.OffsetDateTime;
import lombok.Data;

/**
 * リフレッシュトークン情報を管理するテーブル
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
    private byte[] hashedToken;

    /**
     * 発行日時
     */
    private OffsetDateTime createdAt;

    /**
     * 失効日時
     */
    private OffsetDateTime expiresAt;

    /**
     * 失効フラグ
     */
    private Boolean revoked;
}
