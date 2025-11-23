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
    responseCode = "403",
    description = "当APIの実行権限がない場合",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
)
public @interface ApiForbiddenResponse {}
