package jp.co.itfllc.WebSystemSamples.interceptors;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.itfllc.WebSystemSamples.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
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
            // トークンが存在しないか、Bearerトークンでない場合は401エラーを返します
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return false;
        }

        // BearerトークンからJWTを抽出します
        String token = authHeader.substring(7);

        try {
            // JWTを検証します
            Claims claims = this.jwtUtils.getClaims(token);

            // TODO: DBに有効なユーザーか問い合わせる
            // claimsオブジェクトからユーザー情報を取得する等、必要に応じて処理を追加できます
            System.out.println(claims.getSubject());

            return true;
        } catch (Exception e) {
            // JWTの検証に失敗した場合は401エラーを返します
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return false;
        }
    }
}
