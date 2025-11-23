package jp.co.itfllc.WebSystemSamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Web System Samples クラス
 */
@EnableScheduling
@SpringBootApplication
public class WebSystemSamplesApplication {

    /**
     * エントリーポイント
     *
     * @param args 起動引数
     */
    public static void main(final String[] args) {
        SpringApplication.run(WebSystemSamplesApplication.class, args);
    }
}
