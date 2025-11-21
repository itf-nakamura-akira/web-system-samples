package jp.co.itfllc.WebSystemSamples;

import jp.co.itfllc.WebSystemSamples.interceptors.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 設定クラス
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 認証インターセプター
     */
    @NonNull
    private final AuthInterceptor authInterceptor;

    /**
     * インターセプターを追加します。
     *
     * @param registry インターセプターレジストリ
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(this.authInterceptor).addPathPatterns("/**").excludePathPatterns("/login");
    }
}
