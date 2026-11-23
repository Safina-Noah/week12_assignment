package edu.handong.week12.book;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatus(
            ResponseStatusException exception) {
        String code = exception.getStatusCode().value() == HttpStatus.NOT_FOUND.value()
                ? "404 Not Found"
                : "HTTP_" + exception.getStatusCode().value();
        String message = exception.getReason() != null
                ? exception.getReason()
                : "요청 처리에 실패했습니다.";

        return ResponseEntity.status(exception.getStatusCode())
                .body(Map.of("code", code, "message", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult().getFieldErrors();
        String message = errors.isEmpty()
                ? "입력값이 올바르지 않습니다."
                : errors.get(0).getField() + ": " + errors.get(0).getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(Map.of("code", "400 Bad Request", "message", message));
    }
}
