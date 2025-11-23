package jp.co.itfllc.WebSystemSamples.mappers;

import java.util.Optional;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.RefreshTokensEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * リフレッシュトークン情報を管理するテーブル向け Mapper
 */
@Mapper
public interface RefreshTokensMapper {
    /**
     * リフレッシュトークン情報を挿入する
     *
     * @param refreshTokens 挿入するリフレッシュトークンレコード
     * @return 挿入されたリフレッシュトークンレコード
     */
    RefreshTokensEntity insert(RefreshTokensEntity refreshTokens);

    /**
     * リフレッシュトークン情報を取得する
     *
     * @param hashedToken ハッシュ化されたトークン
     * @return リフレッシュトークン情報
     */
    Optional<RefreshTokensEntity> selectByHashedToken(byte[] hashedToken);

    /**
     * リフレッシュトークン情報を失効済みにする
     *
     * @param hashedToken ハッシュ化されたトークン
     */
    void revokeByHashedToken(byte[] hashedToken);

    /**
     * ユーザーに紐づくリフレッシュトークンを全て失効済みにする
     *
     * @param usersId ユーザーID
     */
    void revokeAllByUsersId(String usersId);
}
