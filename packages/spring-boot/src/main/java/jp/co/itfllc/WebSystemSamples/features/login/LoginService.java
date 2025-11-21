package jp.co.itfllc.WebSystemSamples.features.login;

import jp.co.itfllc.WebSystemSamples.mappers.UsersMapper;
import jp.co.itfllc.WebSystemSamples.mappers.results.entities.UsersEntity;
import jp.co.itfllc.WebSystemSamples.utils.CryptoUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * ログイン画面用 Service
 */
@Service
public class LoginService {

    /**
     * ユーザーテーブル向け Mapper
     */
    private final UsersMapper usersMapper;

    /**
     * コンストラクタ
     *
     * @param usersMapper ユーザーテーブル向け Mapper
     */
    public LoginService(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    /**
     * ログイン処理を行う
     *
     * @param account アカウント
     * @param password パスワード
     * @return ログインユーザー
     */
    public UsersEntity login(String account, String password) {
        UsersEntity user = this.usersMapper.selectByAccount(account);

        if (user == null || !CryptoUtils.verifyPassword(user.getHashedPassword(), password)) {
            // アカウントまたはパスワードが誤っている場合は、401エラーを返却します。
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "アカウントまたはパスワードが誤っています。");
        }

        return user;
    }
}
