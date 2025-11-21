package jp.co.itfllc.WebSystemSamples.features.login;

import jp.co.itfllc.WebSystemSamples.features.login.models.PostLoginRequest;
import jp.co.itfllc.WebSystemSamples.features.login.models.PostLoginResponse;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
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
     * ログイン機能用 Service
     */
    private final LoginService loginService;

    /**
     * コンストラクタ
     *
     * @param loginService ログイン機能用 Service
     */
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
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

        // TODO JWTの生成

        return new PostLoginResponse(loginUser.getName(), loginUser.getHashedPassword());
    }
}
