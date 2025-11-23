package jp.co.itfllc.WebSystemSamples.mappers;

import java.util.List;
import java.util.Optional;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * `users` テーブルへのデータアクセスを提供するMyBatisのマッパーインターフェースです。
 */
@Mapper
public interface UsersMapper {
    /**
     * 指定されたIDに一致するユーザーをデータベースから検索します。
     *
     * @param id 検索対象のユーザーID。
     * @return 見つかったユーザーのエンティティオブジェクト。見つからない場合は `Optional.empty`。
     */
    Optional<UsersEntity> selectById(final String id);

    /**
     * 指定されたアカウント名に一致するユーザーをデータベースから検索します。
     *
     * @param account 検索対象のアカウント名。
     * @return 見つかったユーザーのエンティティオブジェクト。見つからない場合は `Optional.empty`。
     */
    Optional<UsersEntity> selectByAccount(final String account);

    /**
     * 新しいユーザーをデータベースに保存します。
     *
     * @param user 保存するユーザーのエンティティオブジェクト。
     * @return 保存されたユーザーのエンティティオブジェクト。
     */
    UsersEntity insert(final UsersEntity user);

    /**
     * データベースに登録されている全てのユーザーを取得します。
     *
     * @return 全ユーザーのリスト。見つからない場合は空のリスト。
     */
    List<UsersEntity> selectList();
}
