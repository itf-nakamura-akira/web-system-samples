package jp.co.itfllc.WebSystemSamples.mappers;

import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
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
    UsersEntity selectByAccount(String account);

    /**
     * ユーザーを挿入する
     *
     * @param user 挿入するユーザーレコード
     * @return 挿入されたユーザーレコード
     */
    UsersEntity insert(UsersEntity user);
}
