package jp.co.itfllc.WebSystemSamples.interceptors;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * APIリクエストの認証を処理するインターセプターです。
 * AuthorizationヘッダーからJWTを検証し、有効なユーザー情報をリクエスト属性に設定します。
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * JWTの検証やクレーム抽出を行うためのユーティリティクラスです。
     */
    private final JwtUtils jwtUtils;

    /**
     * ユーザー情報をデータベースから取得するためのMyBatisのマッパーです。
     */
    private final UsersMapper usersMapper;

    /**
     * コントローラーメソッドが実行される前にリクエストを捕捉し、JWTの検証とユーザー認証を行います。
     *
     * @param request  現在のHTTPリクエスト。
     * @param response 現在のHTTPレスポンス。
     * @param handler  処理を担当するハンドラー。
     * @return 認証が成功した場合は {@code true} を返し、処理を続行させます。失敗した場合は例外がスローされます。
     * @throws Exception 認証トークンが無効、またはユーザー検証に失敗した場合。
     */
    @Override
    public boolean preHandle(
        @NonNull final HttpServletRequest request,
        @NonNull final HttpServletResponse response,
        @NonNull final Object handler
    ) throws Exception {
        // AuthorizationヘッダーからBearerトークンを取得します
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "認可に失敗しました。再ログインしてください。");
        }

        // BearerトークンからJWTを抽出します
        String token = authHeader.substring(7);

        try {
            // JWTを検証します
            Claims claims = this.jwtUtils.getClaimsFromAccessToken(token);

            // ユーザー情報の取得
            Optional<UsersEntity> userOptional = this.usersMapper.selectById(claims.getSubject());

            if (userOptional.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "認可に失敗しました。ユーザーデータが存在しません。"
                );
            }

            UsersEntity user = userOptional.get();

            if (user.getDisabledAt() != null) {
                throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "認可に失敗しました。ユーザーデータが無効化されています。"
                );
            }

            // 取得したユーザー情報をリクエスト属性に設定します
            // ex) public List<Hoge> getHoge(@RequestAttribute("user") UsersEntity loginUser){...}
            request.setAttribute("user", user);

            return true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "認可に失敗しました。再ログインしてください。");
        }
    }
}
