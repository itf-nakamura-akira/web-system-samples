package jp.co.itfllc.WebSystemSamples.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.itfllc.WebSystemSamples.enums.Role;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 特定のエンドポイントへのアクセスを管理者（Adminロール）を持つユーザーに限定するためのインターセプターです。
 * このインターセプターは {@code AuthInterceptor} の後に実行されることを想定しています。
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    /**
     * コントローラーメソッドが実行される前に、リクエストを横取りして管理者権限のチェックを行います。
     *
     * @param request  現在のHTTPリクエスト。
     * @param response 現在のHTTPレスポンス。
     * @param handler  処理を担当するハンドラー（通常はコントローラーメソッド）。
     * @return ユーザーが管理者であれば {@code true} を返し、処理を続行させます。それ以外の場合は例外がスローされます。
     * @throws Exception 認可に失敗した場合、または予期せぬエラーが発生した場合。
     */
    @Override
    public boolean preHandle(
        @NonNull final HttpServletRequest request,
        @NonNull final HttpServletResponse response,
        @NonNull final Object handler
    ) throws Exception {
        final Object userAttribute = request.getAttribute("user");

        if (userAttribute == null) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "認可に失敗しました。ユーザーデータが存在しません。"
            );
        }

        if (!(userAttribute instanceof UsersEntity)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "予期せぬエラーが発生しました。");
        }

        final UsersEntity user = (UsersEntity) userAttribute;

        if (user.getRole() != Role.Admin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "この操作を実行する権限がありません。");
        }

        return true;
    }
}
