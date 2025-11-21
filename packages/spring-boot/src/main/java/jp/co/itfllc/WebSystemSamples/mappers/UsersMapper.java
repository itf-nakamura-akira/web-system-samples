package jp.co.itfllc.WebSystemSamples.mappers;

import jp.co.itfllc.WebSystemSamples.entities.Users;
import org.apache.ibatis.annotations.Mapper;

/**
 * ユーザーテーブル向け Mapper
 */
@Mapper
public interface UsersMapper {
    /**
     * アカウントでユーザーを取得する
     *
     * @param account アカウント
     * @return ユーザーレコード
     */
    Users selectByAccount(String account);

    /**
     * ユーザーを挿入する
     *
     * @param user 挿入するユーザーレコード
     * @return 挿入されたユーザーレコード
     */
    Users insert(Users user);
}
