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
    public PostLoginResponse postLogin(@RequestBody PostLoginRequest request) throws Exception {
        Tokens tokens = this.loginService.login(request.account(), request.password());

        return new PostLoginResponse(tokens.accessToken(), tokens.refreshToken());
    }
}

/**
 * ログイン認証 リクエストモデル
 */
record PostLoginRequest(String account, String password) {}

/**
 * ログイン認証 レスポンスモデル
 */
record PostLoginResponse(String accessToken, String refreshToken) {}
