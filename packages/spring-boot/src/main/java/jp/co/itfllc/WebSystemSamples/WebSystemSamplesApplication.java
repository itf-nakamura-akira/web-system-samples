package jp.co.itfllc.WebSystemSamples;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Bootアプリケーションのエントリーポイントとなるメインクラスです。
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Web System Samples",
        version = "0.0.0",
        description = "Webシステム開発の学習用サンプルアプリケーションです。"
    )
)
@EnableScheduling
@SpringBootApplication
public class WebSystemSamplesApplication {

    /**
     * アプリケーションを起動するメインメソッドです。
     *
     * @param args コマンドライン引数
     */
    public static void main(final String[] args) {
        SpringApplication.run(WebSystemSamplesApplication.class, args);
    }
}
