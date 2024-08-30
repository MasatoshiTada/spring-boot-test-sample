package com.example.web.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

/**
 * RESTコントローラーの例外を処理するクラスです。
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * バリデーションエラーを処理します。
     * @param ex 例外
     * @param headers レスポンスヘッダー
     * @param status HTTPステータスコード
     * @param request リクエスト
     * @return バリデーションエラーのレスポンス
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        List<String> messages = ex.getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setTitle("バリデーションエラー");
        problemDetail.setDetail("不正な入力です");
        problemDetail.setProperties(Map.of("messages", messages));
        return ResponseEntity.status(statusCode).body(problemDetail);
    }
}
