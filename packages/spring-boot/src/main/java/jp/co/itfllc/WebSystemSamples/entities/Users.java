package jp.co.itfllc.WebSystemSamples.entities;

import java.time.OffsetDateTime;
import jp.co.itfllc.WebSystemSamples.enums.Role;
import lombok.Data;

/**
 * ユーザーテーブル
 */
@Data
public class Users {

    /**
     * ID
     */
    private String id;

    /**
     * アカウント名
     */
    private String account;

    /**
     * ハッシュ化済みパスワード
     */
    private String hashedPassword;

    /**
     * 表示名
     */
    private String name;

    /**
     * 無効化日時
     */
    private OffsetDateTime disabledAt;

    /**
     * 機能(画面・API)の使用権限に関わるロール
     */
    private Role role;
}
