package jp.co.itfllc.WebSystemSamples.mappers;

import java.util.List;
import java.util.Optional;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * ユーザーテーブル向け Mapper
 */
@Mapper
public interface UsersMapper {
    /**
     * IDでユーザーを取得する
     *
     * @param id ID
     * @return ユーザーレコード
     */
    Optional<UsersEntity> selectById(String id);

    /**
     * アカウントでユーザーを取得する
     *
     * @param account アカウント
     * @return ユーザーレコード
     */
    Optional<UsersEntity> selectByAccount(String account);

    /**
     * ユーザーを挿入する
     *
     * @param user 挿入するユーザーレコード
     * @return 挿入されたユーザーレコード
     */
    UsersEntity insert(UsersEntity user);

    /**
     * ユーザーを全件取得する
     *
     * @return ユーザーレコードのリスト
     */
    List<UsersEntity> selectList();
}
