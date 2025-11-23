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
 * 複数の機能で共通して利用されるAPIエンドポイントを提供するコントローラークラスです。
 */
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    /**
     * 共通機能に関連するビジネスロジックを処理するサービスクラスです。
     */
    private final CommonService commonService;

    /**
     * 現在ログインしているユーザーの情報を取得します。
     *
     * @param loginUser 認証インターセプターによってリクエスト属性に設定されたログインユーザーのエンティティ。
     * @return クライアントに返すためのログインユーザー情報（アカウント、名前、ロール）。
     */
    @GetMapping("/loginUser")
    public GetLoginUserResponse getLoginUser(@RequestAttribute("user") final UsersEntity loginUser) {
        return new GetLoginUserResponse(loginUser.getAccount(), loginUser.getName(), loginUser.getRole());
    }

    /**
     * ユーザーのログアウト処理を実行し、関連するリフレッシュトークンを無効化します。
     *
     * @param request   無効化するリフレッシュトークンを含むリクエストボディ。
     * @param loginUser 認証インターセプターによってリクエスト属性に設定されたログインユーザーのエンティティ。
     * @throws Exception ログアウト処理中に例外が発生した場合。
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
 * ログインユーザー情報取得APIのレスポンスボディを表すレコードクラスです。
 *
 * @param account アカウント名
 * @param name    ユーザー名
 * @param role    役割
 */
record GetLoginUserResponse(String account, String name, Role role) {}

/**
 * ログアウトAPIへのリクエストボディを表すレコードクラスです。
 *
 * @param refreshToken リフレッシュトークン
 */
record LogoutRequest(String refreshToken) {}
