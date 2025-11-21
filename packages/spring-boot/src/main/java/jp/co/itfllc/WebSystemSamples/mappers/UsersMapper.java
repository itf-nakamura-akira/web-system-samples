package jp.co.itfllc.WebSystemSamples.mappers;

import jp.co.itfllc.WebSystemSamples.entities.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * ユーザーテーブル向け Mapper
 */
@Mapper
public interface UsersMapper {
    /**
     * アカウントでユーザーを取得する
     *
     * @param account アカウント
     * @return ユーザーレコード
     */
    @Select(
        """
            SELECT
                *
            FROM
                users
            WHERE
                account = #{account};
        """
    )
    Users selectByAccount(String account);

    /**
     * ユーザーを挿入する
     *
     * @param user 挿入するユーザーレコード
     * @return 挿入されたユーザーレコード
     */
    @Select(
        """
            INSERT INTO users
                (account, hashed_password, "name", disabled_at, "role")
            VALUES
                (#{account}, #{hashedPassword}, #{name}, #{disabledAt}, #{role}::role)
            RETURNING
                *;
        """
    )
    Users insert(Users user);
}
