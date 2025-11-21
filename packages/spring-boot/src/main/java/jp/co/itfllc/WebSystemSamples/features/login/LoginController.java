package jp.co.itfllc.WebSystemSamples.features.login;

import java.util.HashMap;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
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
     * JWTユーティリティクラス
     */
    private final JwtUtils jwtUtils;

    /**
     * ログイン認証
     *
     * @param request リクエスト
     * @return JWT
     */
    @PostMapping
    public PostLoginResponse postLogin(@RequestBody PostLoginRequest request) {
        // ログイン認証
        UsersEntity loginUser = this.loginService.login(request.account(), request.password());

        // アクセストークンとリフレッシュトークンを生成します
        String accessToken = this.jwtUtils.generateAccessToken(loginUser.getAccount(), new HashMap<>());
        String refreshToken = this.jwtUtils.generateRefreshToken(loginUser.getAccount(), new HashMap<>());

        return new PostLoginResponse(accessToken, refreshToken);
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
