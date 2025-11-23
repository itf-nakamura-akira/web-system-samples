package jp.co.itfllc.WebSystemSamples.mappers;

import java.util.Optional;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.RefreshTokensEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * `refresh_tokens` テーブルへのデータアクセスを提供するMyBatisのマッパーインターフェースです。
 */
@Mapper
public interface RefreshTokensMapper {
    /**
     * 新しいリフレッシュトークンをデータベースに保存します。
     *
     * @param refreshTokens 保存するリフレッシュトークンのエンティティオブジェクト。
     * @return 保存されたリフレッシュトークンのエンティティオブジェクト。
     */
    RefreshTokensEntity insert(final RefreshTokensEntity refreshTokens);

    /**
     * ハッシュ化されたトークンに一致するリフレッシュトークンをデータベースから検索します。
     *
     * @param hashedToken 検索対象のハッシュ化されたリフレッシュトークン。
     * @return 見つかったリフレッシュトークンのエンティティオブジェクト。見つからない場合は `Optional.empty`。
     */
    Optional<RefreshTokensEntity> selectByHashedToken(final byte[] hashedToken);

    /**
     * 指定されたハッシュ化トークンに一致するリフレッシュトークンを失効させます。
     *
     * @param hashedToken 失効させるリフレッシュトークンのハッシュ値。
     */
    void revokeByHashedToken(final byte[] hashedToken);

    /**
     * 指定されたユーザーIDに関連するすべてのリフレッシュトークンを失効させます。
     *
     * @param usersId 対象ユーザーのID。
     */
    void revokeAllByUsersId(final String usersId);

    /**
     * 指定されたハッシュ化トークンに一致するリフレッシュトークンをデータベースから削除します。
     *
     * @param hashedToken 削除するリフレッシュトークンのハッシュ値。
     */
    void deleteByHashedToken(final byte[] hashedToken);

    /**
     * 有効期限が切れたすべてのリフレッシュトークンをデータベースから削除します。
     */
    void deleteExpiredTokens();
}
