package jp.co.itfllc.WebSystemSamples.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * アプリケーション起動時にSwagger UIのURLをログに出力するコンポーネントです。
 * 開発者がSwagger UIに簡単にアクセスできるように情報を提供します。
 */
@Component
@Slf4j
public class SwaggerUiRunner implements CommandLineRunner {

    /**
     * Swagger UIが有効かどうかを示すフラグです。
     */
    @Value("${springdoc.swagger-ui.enabled}")
    private String privateKeyString;

    /**
     * アプリケーション起動後に実行されるメソッドです。
     * Swagger UIのURLをログに出力します。
     */
    @Override
    public void run(String... args) throws Exception {
        if (this.privateKeyString.equals("true")) {
            log.info(">>> Swagger UI is available at: http://localhost:56080/api/swagger-ui/index.html");
        }
    }
}
