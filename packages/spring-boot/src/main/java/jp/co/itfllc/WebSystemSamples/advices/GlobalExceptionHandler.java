package jp.co.itfllc.WebSystemSamples.advices;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
     * {@code MethodArgumentNotValidException} 型の例外を処理します。
     *
     * @param ex      捕捉された {@code MethodArgumentNotValidException} インスタンス。
     * @param request 現在のリクエストコンテキスト。
     * @return エラー情報を含む {@code ResponseEntity} オブジェクト。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException ex,
        final WebRequest request
    ) {
        final ErrorResponse body = new ErrorResponse(
            LocalDateTime.now(),
            ex.getStatusCode().value(),
            ex
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining())
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

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
        final ErrorResponse body = new ErrorResponse(LocalDateTime.now(), ex.getStatusCode().value(), ex.getReason());

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
        final ErrorResponse body = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "サーバーでエラーが発生しました。"
        );

        // 開発中はスタックトレースをログに出力するとデバッグに便利
        ex.printStackTrace();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
