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
 * 認証インターセプター
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * JWTユーティリティクラス
     */
    private final JwtUtils jwtUtils;

    /**
     * ユーザーテーブル向け Mapper
     */
    private final UsersMapper usersMapper;

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
            Optional<UsersEntity> userOptional = this.usersMapper.selectByAccount(claims.getSubject());

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
