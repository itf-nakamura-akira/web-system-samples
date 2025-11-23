package jp.co.itfllc.WebSystemSamples.features.login;

import java.time.OffsetDateTime;
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
 * 認証関連のビジネスロジックを処理するサービスクラスです。
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    /**
     * JWT（JSON Web Token）の生成や検証を行うユーティリティクラスです。
     */
    private final JwtUtils jwtUtils;

    /**
     * `users` テーブルへのデータアクセスを提供するMyBatisのマッパーです。
     */
    private final UsersMapper usersMapper;

    /**
     * `refresh_tokens` テーブルへのデータアクセスを提供するMyBatisのマッパーです。
     */
    private final RefreshTokensMapper refreshTokensMapper;

    /**
     * ユーザーの認証を行い、成功した場合はアクセストークンとリフレッシュトークンを生成します。
     *
     * @param account  ユーザーのアカウント名。
     * @param password ユーザーのパスワード。
     * @return 生成されたアクセストークンとリフレッシュトークンを含む `Tokens` オブジェクト。
     * @throws Exception 認証失敗時やトークン生成時に発生する可能性のある例外。
     */
    public Tokens login(final String account, final String password) throws Exception {
        final Optional<UsersEntity> user = this.usersMapper.selectByAccount(account);

        if (user.isEmpty() || !CryptoUtils.verifyPassword(user.get().getHashedPassword(), password)) {
            // アカウントまたはパスワードが誤っている場合は、401エラーを返却します。
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "アカウントまたはパスワードが誤っています。");
        }

        // アクセストークンとリフレッシュトークンを生成する
        final String usersId = user.get().getId();
        final String accessToken = this.jwtUtils.generateAccessToken(usersId, new HashMap<>());
        final String refreshToken = this.jwtUtils.generateRefreshToken();

        // リフレッシュトークンをDBに登録する
        final var refreshTokens = new RefreshTokensEntity();
        refreshTokens.setUsersId(usersId);
        refreshTokens.setHashedToken(this.jwtUtils.hashRefreshToken(refreshToken));
        refreshTokens.setExpiresAt(this.jwtUtils.getExpiresAt());

        this.refreshTokensMapper.insert(refreshTokens);

        return new Tokens(accessToken, refreshToken);
    }

    /**
     * 指定されたリフレッシュトークンを検証し、新しいアクセストークンとリフレッシュトークンを生成します。
     *
     * @param refreshToken 更新に使用するリフレッシュトークン。
     * @return 新しく生成されたアクセストークンとリフレッシュトークンを含む `Tokens` オブジェクト。
     * @throws Exception トークンの検証失敗時や再生成時に発生する可能性のある例外。
     */
    public Tokens refreshTokens(final String refreshToken) throws Exception {
        // リフレッシュトークンをハッシュ化する
        final byte[] hashedToken = this.jwtUtils.hashRefreshToken(refreshToken);

        // リフレッシュトークンをDBから取得する
        final Optional<RefreshTokensEntity> token = this.refreshTokensMapper.selectByHashedToken(hashedToken);

        // トークンが存在しない場合は、401エラーを返却します
        if (token.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "セッションが有効期限切れです。再ログインしてください。"
            );
        }

        // トークンが失効済みの場合は不正なリクエストとみなし、該当ユーザーのリフレッシュトークンを全て失効させます
        // 正規のクライアントから多重リクエストがくることも想定して、メッセージは通常のエラーとして返却します
        if (token.get().getRevoked()) {
            this.refreshTokensMapper.revokeAllByUsersId(token.get().getUsersId());

            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "セッションが有効期限切れです。再ログインしてください。"
            );
        }

        // トークンが有効期限切れの場合は、401エラーを返却します
        if (token.get().getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "セッションが有効期限切れです。再ログインしてください。"
            );
        }

        // 古いリフレッシュトークンを失効済みにする
        this.refreshTokensMapper.revokeByHashedToken(hashedToken);

        // 新しいアクセストークンとリフレッシュトークンを生成する
        final String usersId = token.get().getUsersId();
        final String newAccessToken = this.jwtUtils.generateAccessToken(usersId, new HashMap<>());
        final String newRefreshToken = this.jwtUtils.generateRefreshToken();

        // 新しいリフレッシュトークンをDBに登録する
        final var newRefreshTokens = new RefreshTokensEntity();
        newRefreshTokens.setUsersId(usersId);
        newRefreshTokens.setHashedToken(this.jwtUtils.hashRefreshToken(newRefreshToken));
        newRefreshTokens.setExpiresAt(this.jwtUtils.getExpiresAt());

        this.refreshTokensMapper.insert(newRefreshTokens);

        return new Tokens(newAccessToken, newRefreshToken);
    }
}

/**
 * アクセストークンとリフレッシュトークンを保持するレコードクラスです。
 *
 * @param accessToken  JWTアクセストークン
 * @param refreshToken JWTリフレッシュトークン
 */
record Tokens(String accessToken, String refreshToken) {}
