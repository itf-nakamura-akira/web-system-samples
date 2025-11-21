package jp.co.itfllc.WebSystemSamples.features.masters.users;

import java.util.List;
import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ユーザー管理機能 サービスクラス
 */
@Service
@RequiredArgsConstructor
public class UsersService {

    /**
     * ユーザーテーブル向け Mapper
     */
    private final UsersMapper usersMapper;

    /**
     * ユーザーを全件取得する
     *
     * @return ユーザーレコードのリスト
     */
    public List<UsersEntity> getList() {
        return this.usersMapper.selectList();
    }
}
