package jp.co.itfllc.WebSystemSamples.features.login;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jp.co.itfllc.WebSystemSamples.advices.ApiUnauthorizedResponse;
import jp.co.itfllc.WebSystemSamples.advices.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 認証関連のAPIエンドポイントを提供するコントローラークラスです。
 */
@Tag(name = "login")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    /**
     * 認証ロジックを処理するサービスクラスです。
     */
    private final LoginService loginService;

    @Operation(
        summary = "ログインAPI",
        description = "ユーザーのログイン認証を行い、認証トークンを返却します。",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "ログイン成功時",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthTokenResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "アカウントまたはパスワードが誤っている時",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ErrorResponse.class,
                        example = """
                            {
                                "timestamp": "2024-01-01T12:00:00",
                                "status": 401,
                                "message": "アカウントまたはパスワードが誤っています。"
                            }
                        """
                    )
                )
            ),
        }
    )
    @PostMapping
    public AuthTokenResponse login(@RequestBody @Validated final LoginRequest request) throws Exception {
        final Tokens tokens = this.loginService.login(request.account(), request.password());

        return new AuthTokenResponse(tokens.accessToken(), tokens.refreshToken());
    }

    @Operation(
        summary = "トークンリフレッシュAPI",
        description = "リフレッシュトークンを使用して、新しい認証トークンを取得します。",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "リフレッシュ成功時",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthTokenResponse.class)
                )
            ),
        }
    )
    @ApiUnauthorizedResponse
    @PostMapping("/refresh")
    public AuthTokenResponse refreshAuthToken(@RequestBody @Validated final RefreshRequest request) throws Exception {
        final Tokens tokens = this.loginService.refreshTokens(request.refreshToken());

        return new AuthTokenResponse(tokens.accessToken(), tokens.refreshToken());
    }
}

@Schema(description = "ログインAPIのリクエストボディ")
record LoginRequest(
    @Schema(description = "アカウント名", example = "nakamura.akira")
    @NotBlank(message = "アカウント名が入力されていません。")
    String account,
    @Schema(description = "パスワード", example = "password")
    @NotBlank(message = "パスワードが入力されていません。")
    String password
) {}

@Schema(description = "トークンリフレッシュAPIのリクエストボディ")
record RefreshRequest(
    @Schema(description = "リフレッシュトークン", example = "2OblN1pZsfztw52D4AhXYp7wvvfnuJuJ...")
    @NotBlank(message = "リフレッシュトークンが入力されていません。")
    String refreshToken
) {}

@Schema(description = "認証トークンレスポンス")
record AuthTokenResponse(
    @Schema(description = "アクセストークン", example = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJp...") String accessToken,
    @Schema(description = "リフレッシュトークン", example = "2OblN1pZsfztw52D4AhXYp7wvvfnuJuJ...") String refreshToken
) {}
