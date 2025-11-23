package jp.co.itfllc.WebSystemSamples.features.masters.users;

import java.time.OffsetDateTime;
import java.util.List;
import jp.co.itfllc.WebSystemSamples.enums.Role;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ユーザー管理機能 コントローラークラス
 */
@RestController
@RequestMapping("/masters/users")
@RequiredArgsConstructor
public class UsersController {

    /**
     * ユーザー管理機能 サービスクラス
     */
    private final UsersService usersService;

    /**
     * ユーザーを全件取得する
     *
     * @return ユーザーレコードのリスト
     */
    @GetMapping
    public GetListResponse getList() {
        final List<UsersEntity> users = this.usersService.getList();

        return new GetListResponse(
            users
                .stream()
                .map(user ->
                    new Users(user.getId(), user.getAccount(), user.getName(), user.getDisabledAt(), user.getRole())
                )
                .toList()
        );
    }
}

/**
 * ユーザーを全件取得 レスポンスモデル
 */
record GetListResponse(List<Users> users) {}

/**
 * ユーザー レスポンスモデル
 */
record Users(String id, String account, String name, OffsetDateTime disabledAt, Role role) {}
