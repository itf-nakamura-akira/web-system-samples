package jp.co.itfllc.WebSystemSamples.features.masters.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.time.OffsetDateTime;
import java.util.List;
import jp.co.itfllc.WebSystemSamples.advices.ApiForbiddenResponse;
import jp.co.itfllc.WebSystemSamples.advices.ApiUnauthorizedResponse;
import jp.co.itfllc.WebSystemSamples.enums.Role;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ユーザーマスター管理機能に関するAPIエンドポイントを提供するコントローラークラスです。
 */
@RestController
@RequestMapping("/masters/users")
@RequiredArgsConstructor
public class UsersController {

    /**
     * ユーザー管理に関連するビジネスロジックを処理するサービスクラスです。
     */
    private final UsersService usersService;

    @Operation(
        summary = "ユーザー一覧取得API",
        description = "登録されているすべてのユーザー情報を取得します。",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "取得成功時",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsersResponse.class)
                )
            ),
        }
    )
    @ApiUnauthorizedResponse
    @ApiForbiddenResponse
    @GetMapping
    public UsersResponse getMastersUsers() {
        final List<UsersEntity> users = this.usersService.getList();

        return new UsersResponse(
            users
                .stream()
                .map(user ->
                    new UserRecord(
                        user.getId(),
                        user.getAccount(),
                        user.getName(),
                        user.getDisabledAt(),
                        user.getRole()
                    )
                )
                .toList()
        );
    }
}

@Schema(description = "ユーザー一覧取得APIのレスポンス")
record UsersResponse(@Schema(description = "ユーザー情報のリスト") List<UserRecord> users) {}

@Schema(description = "ユーザー情報")
record UserRecord(
    @Schema(description = "ユーザーID") String id,
    @Schema(description = "アカウント名") String account,
    @Schema(description = "ユーザー名") String name,
    @Schema(description = "無効化された日時") OffsetDateTime disabledAt,
    @Schema(description = "役割") Role role
) {}
