package jp.co.itfllc.WebSystemSamples.mappers.results.entities;

import java.time.OffsetDateTime;
import jp.co.itfllc.WebSystemSamples.enums.Role;
import lombok.Data;

/**
 * `users` テーブルのレコードを表すエンティティクラスです。
 */
@Data
public class UsersEntity {

    /**
     * 主キーとなるユーザーID。
     */
    private String id;

    /**
     * ログイン時に使用される一意のアカウント名。
     */
    private String account;

    /**
     * Argon2でハッシュ化されたパスワード。
     */
    private String hashedPassword;

    /**
     * 画面に表示されるユーザーの名前。
     */
    private String name;

    /**
     * ユーザーアカウントが無効化された日時。有効な場合は {@code null}。
     */
    private OffsetDateTime disabledAt;

    /**
     * ユーザーの権限レベルを示す役割（ロール）。
     */
    private Role role;
}
