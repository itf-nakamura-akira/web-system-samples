package jp.co.itfllc.WebSystemSamples.mappers.results.entities;

import java.time.OffsetDateTime;
import lombok.Data;

/**
 * `refresh_tokens` テーブルのレコードを表すエンティティクラスです。
 */
@Data
public class RefreshTokensEntity {

    /**
     * 主キーとなるID。
     */
    private String id;

    /**
     * このリフレッシュトークンを発行したユーザーのID。
     */
    private String usersId;

    /**
     * SHA-512でハッシュ化されたリフレッシュトークン。
     */
    private byte[] hashedToken;

    /**
     * このトークンが発行された日時。
     */
    private OffsetDateTime createdAt;

    /**
     * このトークンの有効期限が切れる日時。
     */
    private OffsetDateTime expiresAt;

    /**
     * このトークンが失効しているかどうかを示すフラグ。
     */
    private Boolean revoked;
}
