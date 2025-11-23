package jp.co.itfllc.WebSystemSamples.features.common;

import jp.co.itfllc.WebSystemSamples.enums.Role;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 共通機能 コントローラークラス
 */
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    /**
     * 共通機能 サービスクラス
     */
    private final CommonService commonService;

    /**
     * ログインユーザー情報を取得する
     *
     * @param loginUser ログインユーザー情報 (JWT の認可処理で取得)
     * @return ログインユーザー情報
     */
    @GetMapping("/loginUser")
    public GetLoginUserResponse getLoginUser(@RequestAttribute("user") final UsersEntity loginUser) {
        return new GetLoginUserResponse(loginUser.getAccount(), loginUser.getName(), loginUser.getRole());
    }

    /**
     * ログアウト処理を行う
     *
     * @param request リクエスト
     * @param loginUser ログインユーザー情報 (JWT の認可処理で取得)
     * @throws Exception
     */
    @PostMapping("/logout")
    public void postLogout(
        @RequestBody final LogoutRequest request,
        @RequestAttribute("user") final UsersEntity loginUser
    ) throws Exception {
        this.commonService.logout(request.refreshToken(), loginUser);
    }
}

/**
 * ログインユーザー レスポンスモデル
 */
record GetLoginUserResponse(String account, String name, Role role) {}

/**
 * ログアウトAPI リクエストモデル
 */
record LogoutRequest(String refreshToken) {}
