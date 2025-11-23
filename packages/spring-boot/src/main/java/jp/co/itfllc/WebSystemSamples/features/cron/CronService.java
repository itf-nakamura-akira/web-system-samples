package jp.co.itfllc.WebSystemSamples.features.cron;

import jp.co.itfllc.WebSystemSamples.mappers.RefreshTokensMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Cron 処理を行う Service クラス
 */
@Service
@RequiredArgsConstructor
public class CronService {

    /**
     * リフレッシュトークン情報を管理するテーブル向け Mapper
     */
    private final RefreshTokensMapper refreshTokensMapper;

    /**
     * 古いリフレッシュトークンを削除する
     * 毎日日本時間午前3時に実行
     */
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Tokyo")
    public void deleteExpiredRefreshTokens() {
        this.refreshTokensMapper.deleteExpiredTokens();
    }
}
