package org.example.cathayjohnsonchin.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.cathayjohnsonchin.dto.general.ErrorResponse;
import org.example.cathayjohnsonchin.model.ResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(ApiException e) {
        ResultCode code = e.getResultCode();

        log.warn("Api exception occurred. code={}, message={}", code.getCode(), e.getMessage());

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ErrorResponse.builder()
                        .resultCode(code.getCode())
                        .resultMessage(code.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled exception", e);

        ResultCode code = ResultCode.SYSTEM_ERROR;

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ErrorResponse.builder()
                        .resultCode(code.getCode())
                        .resultMessage(code.getMessage())
                        .build());
    }


}
