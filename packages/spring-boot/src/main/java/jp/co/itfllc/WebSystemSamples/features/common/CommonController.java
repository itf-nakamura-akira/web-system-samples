package jp.co.itfllc.WebSystemSamples.features.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import jp.co.itfllc.WebSystemSamples.advices.ApiUnauthorizedResponse;
import jp.co.itfllc.WebSystemSamples.enums.Role;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 複数の機能で共通して利用されるAPIエンドポイントを提供するコントローラークラスです。
 */
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    /**
     * 共通機能に関連するビジネスロジックを処理するサービスクラスです。
     */
    private final CommonService commonService;

    @Operation(
        summary = "ログインユーザー情報取得API",
        description = "現在ログインしているユーザーの情報を取得します。",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "取得成功時",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetLoginUserResponse.class)
                )
            ),
        }
    )
    @ApiUnauthorizedResponse
    @GetMapping("/loginUser")
    public GetLoginUserResponse getLoginUser(@RequestAttribute("user") final UsersEntity loginUser) {
        return new GetLoginUserResponse(loginUser.getAccount(), loginUser.getName(), loginUser.getRole());
    }

    @Operation(
        summary = "ログアウトAPI",
        description = "ユーザーのログアウト処理を実行し、関連するリフレッシュトークンを無効化します。",
        responses = { @ApiResponse(responseCode = "200", description = "ログアウト成功時") }
    )
    @ApiUnauthorizedResponse
    @PostMapping("/logout")
    public void postLogout(
        @RequestBody @Validated final LogoutRequest request,
        @RequestAttribute("user") final UsersEntity loginUser
    ) throws Exception {
        this.commonService.logout(request.refreshToken(), loginUser);
    }
}

@Schema(description = "ログインユーザー情報取得APIのレスポンス")
record GetLoginUserResponse(
    @Schema(description = "アカウント名") String account,
    @Schema(description = "ユーザー名") String name,
    @Schema(description = "役割") Role role
) {}

@Schema(description = "ログアウトAPIへのリクエスト")
record LogoutRequest(
    @Schema(description = "リフレッシュトークン")
    @NotBlank(message = "リフレッシュトークンが入力されていません。")
    String refreshToken
) {}
