package jp.co.itfllc.WebSystemSamples.advices;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
    responseCode = "401",
    description = "認証トークンが無効、または有効期限切れの場合",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(
            implementation = ErrorResponse.class,
            example = """
                {
                    "timestamp": "2024-01-01T12:00:00",
                    "status": 401,
                    "message": "認証トークンが無効、または有効期限切れです。"
                }
            """
        )
    )
)
public @interface ApiUnauthorizedResponse {}
