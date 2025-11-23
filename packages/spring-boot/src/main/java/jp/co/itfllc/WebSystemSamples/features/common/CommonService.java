package jp.co.itfllc.WebSystemSamples.features.common;

import jp.co.itfllc.WebSystemSamples.mappers.RefreshTokensMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * 複数の機能で共通して利用されるビジネスロジックを処理するサービスクラスです。
 */
@Service
@RequiredArgsConstructor
public class CommonService {

    /**
     * JWTのハッシュ化など、関連操作を行うためのユーティリティクラスです。
     */
    private final JwtUtils jwtUtils;

    /**
     * `refresh_tokens` テーブルへのデータアクセスを提供するMyBatisのマッパーです。
     */
    private final RefreshTokensMapper refreshTokensMapper;

    /**
     * ユーザーのログアウト処理を行い、指定されたリフレッシュトークンをデータベースから削除します。
     *
     * @param refreshToken 削除対象のリフレッシュトークン。
     * @param loginUser    現在ログインしているユーザーのエンティティ。
     * @throws Exception リフレッシュトークンのハッシュ化で例外が発生した場合。
     */
    public void logout(final String refreshToken, final UsersEntity loginUser) throws Exception {
        // リフレッシュトークンをハッシュ化する
        final byte[] hashedToken = this.jwtUtils.hashRefreshToken(refreshToken);

        // リフレッシュトークンをDBから取得する
        final var token = this.refreshTokensMapper.selectByHashedToken(hashedToken);

        // トークンが存在しない場合は、何もせずに終了
        if (token.isEmpty()) {
            return;
        }

        // トークンの所有者とログインユーザーが一致しない場合は、不正なリクエストとみなし、
        // ログインユーザーに紐づくリフレッシュトークンを全て失効させ、401エラーを返却する
        if (!token.get().getUsersId().equals(loginUser.getId())) {
            this.refreshTokensMapper.revokeAllByUsersId(loginUser.getId());

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "不正なリクエストです。");
        }

        // リフレッシュトークンをDBから削除する
        this.refreshTokensMapper.deleteByHashedToken(hashedToken);
    }
}
