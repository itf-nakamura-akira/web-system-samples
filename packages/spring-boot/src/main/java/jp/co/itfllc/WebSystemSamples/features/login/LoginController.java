package jp.co.itfllc.WebSystemSamples.features.login;

import java.util.HashMap;
import jp.co.itfllc.WebSystemSamples.features.login.models.PostLoginRequest;
import jp.co.itfllc.WebSystemSamples.features.login.models.PostLoginResponse;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ログイン画面用 Controller
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    /**
     * ログイン画面用 Service
     */
    private final LoginService loginService;

    /**
     * JWTユーティリティクラス
     */
    private final JwtUtils jwtUtils;

    /**
     * コンストラクタ
     *
     * @param loginService ログイン機能用 Service
     * @param jwtUtils JWTユーティリティ
     */
    public LoginController(LoginService loginService, JwtUtils jwtUtils) {
        this.loginService = loginService;
        this.jwtUtils = jwtUtils;
    }

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
