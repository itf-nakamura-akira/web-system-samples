package jp.co.itfllc.WebSystemSamples.advices;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

/**
 * アプリケーション全体で発生する例外を横断的に処理するためのアドバイスクラスです。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * {@code ResponseStatusException} 型の例外を処理します。
     * この例外は、特定のHTTPステータスコードとエラーメッセージをクライアントに返すために使用されます。
     *
     * @param ex      捕捉された {@code ResponseStatusException} インスタンス。
     * @param request 現在のリクエストコンテキスト。
     * @return エラー情報を含む {@code ResponseEntity} オブジェクト。
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(
        final ResponseStatusException ex,
        final WebRequest request
    ) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatusCode().value());
        body.put("message", ex.getReason());

        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    /**
     * 他のハンドラーで捕捉されなかったすべての {@code Exception} 型の例外を処理します。
     * 予期せぬサーバーエラーとして扱われます。
     *
     * @param ex      捕捉された {@code Exception} インスタンス。
     * @param request 現在のリクエストコンテキスト。
     * @return 固定のエラーメッセージとHTTP 500ステータスを含む {@code ResponseEntity} オブジェクト。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(final Exception ex, final WebRequest request) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("message", "サーバーでエラーが発生しました。");

        // 開発中はスタックトレースをログに出力するとデバッグに便利
        ex.printStackTrace();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
