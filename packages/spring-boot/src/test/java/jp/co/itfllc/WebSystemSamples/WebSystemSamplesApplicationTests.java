package jp.co.itfllc.WebSystemSamples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "file:/workspace/.devcontainer/app/.env")
class WebSystemSamplesApplicationTests {

    @Test
    @DisplayName("アプリ起動確認")
    void contextLoads() {}
}
