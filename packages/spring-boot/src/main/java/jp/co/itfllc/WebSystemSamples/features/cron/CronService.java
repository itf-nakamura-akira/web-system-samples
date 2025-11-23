package jp.co.itfllc.WebSystemSamples.features.cron;

import jp.co.itfllc.WebSystemSamples.mappers.RefreshTokensMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 定期的なバッチ処理（Cronジョブ）を管理するサービスクラスです。
 */
@Service
@RequiredArgsConstructor
public class CronService {

    /**
     * `refresh_tokens` テーブルへのアクセスを提供するMyBatisのマッパーです。
     */
    private final RefreshTokensMapper refreshTokensMapper;

    /**
     * 有効期限が切れたリフレッシュトークンをデータベースから削除します。
     * この処理は、毎日日本時間午前3時に自動的に実行されます。
     */
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Tokyo")
    public void deleteExpiredRefreshTokens() {
        this.refreshTokensMapper.deleteExpiredTokens();
    }
}
