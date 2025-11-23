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

    /**
     * 登録されているすべてのユーザー情報を取得します。
     *
     * @return 全ユーザーの情報を含むレスポンスオブジェクト。
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
 * ユーザー一覧取得APIのレスポンスボディを表すレコードクラスです。
 *
 * @param users ユーザー情報のリスト
 */
record GetListResponse(List<Users> users) {}

/**
 * レスポンスに含める個々のユーザー情報を表すレコードクラスです。
 *
 * @param id         ユーザーID
 * @param account    アカウント名
 * @param name       ユーザー名
 * @param disabledAt 無効化された日時
 * @param role       役割
 */
record Users(String id, String account, String name, OffsetDateTime disabledAt, Role role) {}
