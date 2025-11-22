package jp.co.itfllc.WebSystemSamples.features.login;

import java.util.HashMap;
import java.util.Optional;
import jp.co.itfllc.WebSystemSamples.mappers.RefreshTokensMapper;
import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.RefreshTokensEntity;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.CryptoUtils;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * ログイン画面 サービスクラス
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    /**
     * JWTユーティリティクラス
     */
    private final JwtUtils jwtUtils;

    /**
     * ユーザーテーブル向け Mapper
     */
    private final UsersMapper usersMapper;

    /**
     * JWT(リフレッシュトークン)を管理するテーブル向け Mapper
     */
    private final RefreshTokensMapper refreshTokensMapper;

    /**
     * ログイン処理を行う
     *
     * @param account アカウント
     * @param password パスワード
     * @return ログインユーザー
     * @throws Exception
     */
    public Tokens login(String account, String password) throws Exception {
        Optional<UsersEntity> user = this.usersMapper.selectByAccount(account);

        if (user.isEmpty() || !CryptoUtils.verifyPassword(user.get().getHashedPassword(), password)) {
            // アカウントまたはパスワードが誤っている場合は、401エラーを返却します。
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "アカウントまたはパスワードが誤っています。");
        }

        // アクセストークンとリフレッシュトークンを生成する
        String usersId = user.get().getId();
        String accessToken = this.jwtUtils.generateAccessToken(usersId, new HashMap<>());
        String refreshToken = this.jwtUtils.generateRefreshToken();

        // リフレッシュトークンをDBに登録する
        var refreshTokens = new RefreshTokensEntity();
        refreshTokens.setUsersId(usersId);
        refreshTokens.setHashedToken(this.jwtUtils.hashRefreshToken(refreshToken));
        refreshTokens.setExpiresAt(this.jwtUtils.getExpiresAt());

        this.refreshTokensMapper.insert(refreshTokens);

        return new Tokens(accessToken, refreshToken);
    }
}

/**
 * 認証トークン
 */
record Tokens(String accessToken, String refreshToken) {}
