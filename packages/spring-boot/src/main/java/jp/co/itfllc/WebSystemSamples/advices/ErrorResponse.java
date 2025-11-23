package jp.co.itfllc.WebSystemSamples.advices;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "エラーレスポンス")
public record ErrorResponse(
    @Schema(description = "タイムスタンプ") LocalDateTime timestamp,
    @Schema(description = "HTTPステータスコード") int status,
    @Schema(description = "エラーメッセージ") String message
) {}
