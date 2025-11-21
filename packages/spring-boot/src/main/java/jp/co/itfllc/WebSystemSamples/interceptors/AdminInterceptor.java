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
 * 管理者権限チェックインターセプター
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    /**
     * リクエストのプリハンドリングを行います。
     *
     * @param request  HTTPリクエスト
     * @param response HTTPレスポンス
     * @param handler  ハンドラー
     * @return 処理を続行するかどうか
     * @throws Exception 例外
     */
    @Override
    public boolean preHandle(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler
    ) throws Exception {
        Object userAttribute = request.getAttribute("user");

        if (userAttribute == null) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "認可に失敗しました。ユーザーデータが存在しません。"
            );
        }

        if (!(userAttribute instanceof UsersEntity)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "予期せぬエラーが発生しました。");
        }

        UsersEntity user = (UsersEntity) userAttribute;

        if (user.getRole() != Role.Admin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "この操作を実行する権限がありません。");
        }

        return true;
    }
}
