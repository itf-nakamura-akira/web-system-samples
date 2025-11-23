package jp.co.itfllc.WebSystemSamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Bootアプリケーションのエントリーポイントとなるメインクラスです。
 */
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
