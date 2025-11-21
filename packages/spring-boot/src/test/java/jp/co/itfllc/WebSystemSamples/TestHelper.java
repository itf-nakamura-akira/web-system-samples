package jp.co.itfllc.WebSystemSamples;

import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * テストユーティリティクラス
 */
@Component
public class TestHelper {

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 指定されたアカウントを持つユーザー情報を取得します。
     *
     * @param account 取得するユーザーのアカウント
     * @return ユーザー情報。ユーザーが存在しない場合はnullを返します。
     */
    public UsersEntity getUserByAccount(String account) {
        return usersMapper.selectByAccount(account).get();
    }
}
