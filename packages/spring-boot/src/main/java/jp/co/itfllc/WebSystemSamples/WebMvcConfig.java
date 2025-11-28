package jp.co.itfllc.WebSystemSamples;

import jp.co.itfllc.WebSystemSamples.interceptors.AdminInterceptor;
import jp.co.itfllc.WebSystemSamples.interceptors.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web MVCのカスタマイズ設定を行うための構成クラスです。
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * CORS(Cross-Origin Resource Sharing)のオリジンを定義するプロパティです。
     * 環境変数 `CORS_ORIGIN` から値が注入されます。
     */
    @Value("${CORS_ORIGIN}")
    private String corsOrigin;

    /**
     * リクエストの認証を処理するインターセプターです。
     */
    @NonNull
    private final AuthInterceptor authInterceptor;

    /**
     * 管理者権限を要求するエンドポイントへのアクセスを制御するインターセプターです。
     */
    @NonNull
    private final AdminInterceptor adminInterceptor;

    /**
     * アプリケーションにインターセプターを登録します。
     * ここでは、認証チェックと管理者権限チェックのインターセプターを特定のエンドポイントに適用しています。
     *
     * @param registry インターセプターを登録するためのレジストリ。
     */
    @Override
    public void addInterceptors(@NonNull final InterceptorRegistry registry) {
        registry
            .addInterceptor(this.authInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/login/**", "/swagger-ui/**", "/api-docs/**");
        registry.addInterceptor(this.adminInterceptor).addPathPatterns("/masters/**");
    }

    /**
     * CORS(Cross-Origin Resource Sharing)設定を追加します。
     * これにより、異なるオリジンからのリクエストを許可します。
     *
     * @param registry CORS設定を登録するためのレジストリ。
     */
    @Override
    public void addCorsMappings(@NonNull final CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins(this.corsOrigin)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
