package jp.co.itfllc.WebSystemSamples.features.login;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 認証関連のAPIエンドポイントを提供するコントローラークラスです。
 */
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    /**
     * 認証ロジックを処理するサービスクラスです。
     */
    private final LoginService loginService;

    /**
     * ユーザーのログイン認証を行い、認証トークンを返却します。
     *
     * @param request ログイン情報（アカウントとパスワード）を含むリクエストボディ。
     * @return 認証トークン（アクセストークンとリフレッシュトークン）を含むレスポンス。
     * @throws Exception
     */
    @PostMapping
    public AuthTokenResponse postLogin(@RequestBody final LoginRequest request) throws Exception {
        final Tokens tokens = this.loginService.login(request.account(), request.password());

        return new AuthTokenResponse(tokens.accessToken(), tokens.refreshToken());
    }

    /**
     * リフレッシュトークンを使用して、新しい認証トークンを取得します。
     *
     * @param request リフレッシュトークンを含むリクエストボディ。
     * @return 新しい認証トークン（アクセストークンとリフレッシュトークン）を含むレスポンス。
     * @throws Exception
     */
    @PostMapping("/refresh")
    public AuthTokenResponse postRefresh(@RequestBody final RefreshRequest request) throws Exception {
        final Tokens tokens = this.loginService.refreshTokens(request.refreshToken());

        return new AuthTokenResponse(tokens.accessToken(), tokens.refreshToken());
    }
}

/**
 * ログインAPIへのリクエストボディを表すレコードクラスです。
 *
 * @param account  アカウント名
 * @param password パスワード
 */
record LoginRequest(String account, String password) {}

/**
 * トークンリフレッシュAPIへのリクエストボディを表すレコードクラスです。
 *
 * @param refreshToken リフレッシュトークン
 */
record RefreshRequest(String refreshToken) {}

/**
 * 認証APIのレスポンスボディを表すレコードクラスです。
 *
 * @param accessToken  アクセストークン
 * @param refreshToken リフレッシュトークン
 */
record AuthTokenResponse(String accessToken, String refreshToken) {}
