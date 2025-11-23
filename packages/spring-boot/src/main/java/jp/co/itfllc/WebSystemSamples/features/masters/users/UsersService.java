package jp.co.itfllc.WebSystemSamples.features.masters.users;

import java.util.List;
import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ユーザーマスター管理に関連するビジネスロジックを処理するサービスクラスです。
 */
@Service
@RequiredArgsConstructor
public class UsersService {

    /**
     * `users` テーブルへのデータアクセスを提供するMyBatisのマッパーです。
     */
    private final UsersMapper usersMapper;

    /**
     * 登録されているすべてのユーザー情報を取得します。
     *
     * @return 全ユーザーのエンティティリスト。見つからない場合は空のリスト。
     */
    public List<UsersEntity> getList() {
        return this.usersMapper.selectList();
    }
}
