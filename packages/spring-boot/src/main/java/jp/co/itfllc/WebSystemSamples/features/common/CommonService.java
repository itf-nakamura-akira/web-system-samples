package jp.co.itfllc.WebSystemSamples.features.common;

import jp.co.itfllc.WebSystemSamples.mappers.RefreshTokensMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * 共通機能 サービスクラス
 */
@Service
@RequiredArgsConstructor
public class CommonService {

    /**
     * JWTユーティリティクラス
     */
    private final JwtUtils jwtUtils;

    /**
     * JWT(リフレッシュトークン)を管理するテーブル向け Mapper
     */
    private final RefreshTokensMapper refreshTokensMapper;

    /**
     * ログアウト処理を行う
     *
     * @param refreshToken リフレッシュトークン
     * @param loginUser ログインユーザー情報
     * @throws Exception
     */
    public void logout(String refreshToken, UsersEntity loginUser) throws Exception {
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
