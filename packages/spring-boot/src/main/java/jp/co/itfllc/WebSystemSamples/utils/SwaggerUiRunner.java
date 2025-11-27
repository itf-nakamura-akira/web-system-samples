package jp.co.itfllc.WebSystemSamples.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

            try {
                // HTTPクライアントを作成
                HttpClient client = HttpClient.newHttpClient();

                // HTTPリクエストを作成
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/api-docs"))
                    .build();

                // HTTPリクエストを送信し、レスポンスを文字列として取得
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // レスポンスのボディを取得
                String body = response.body();

                // ObjectMapperを作成
                ObjectMapper mapper = new ObjectMapper();

                // JSONを整形
                Object json = mapper.readValue(body, Object.class);
                String formattedBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

                // 出力先のファイルパスを指定
                Path path = Paths.get("/workspace/packages/spring-boot/schema.json");

                // ファイルに書き込み
                Files.writeString(path, formattedBody);

                log.info(">>> OpenAPI schema has been generated at: " + path.toAbsolutePath());
            } catch (Exception e) {
                log.error("Failed to generate OpenAPI schema.", e);
            }
        }
    }
}
