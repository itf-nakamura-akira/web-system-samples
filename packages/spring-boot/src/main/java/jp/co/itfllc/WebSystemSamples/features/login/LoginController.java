package jp.co.itfllc.WebSystemSamples.features.login;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ログイン画面 コントローラークラス
 */
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    /**
     * ログイン画面 サービスクラス
     */
    private final LoginService loginService;

    /**
     * ログイン認証
     *
     * @param request リクエスト
     * @return JWT
     * @throws Exception
     */
    @PostMapping
    public AuthTokenResponse postLogin(@RequestBody LoginRequest request) throws Exception {
        Tokens tokens = this.loginService.login(request.account(), request.password());

        return new AuthTokenResponse(tokens.accessToken(), tokens.refreshToken());
    }

    /**
     * アクセストークンをリフレッシュします
     *
     * @param request リクエスト
     * @return JWT
     * @throws Exception
     */
    @PostMapping("/refresh")
    public AuthTokenResponse postRefresh(@RequestBody RefreshRequest request) throws Exception {
        Tokens tokens = this.loginService.refreshTokens(request.refreshToken());

        return new AuthTokenResponse(tokens.accessToken(), tokens.refreshToken());
    }
}

/**
 * ログイン認証 リクエストモデル
 */
record LoginRequest(String account, String password) {}

/**
 * リフレッシュAPI リクエストモデル
 */
record RefreshRequest(String refreshToken) {}

/**
 * 認証トークン レスポンスモデル
 */
record AuthTokenResponse(String accessToken, String refreshToken) {}
