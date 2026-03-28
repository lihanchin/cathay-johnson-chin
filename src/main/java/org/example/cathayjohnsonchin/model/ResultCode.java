package org.example.cathayjohnsonchin.model;

import org.springframework.http.HttpStatus;

public enum ResultCode {

    BAD_REQUEST("1000", "請求參數錯誤", HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED("1001", "欄位驗證失敗", HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT("1002", "不合法的請求參數", HttpStatus.BAD_REQUEST),

    NOT_FOUND("4000", "查無資料", HttpStatus.NOT_FOUND),
    DUPLICATE_DATA("4001", "資料已存在", HttpStatus.CONFLICT),

    EXTERNAL_API_ERROR("5001", "外部服務異常", HttpStatus.BAD_GATEWAY),
    EXTERNAL_API_NO_DATA("5002", "外部服務未回傳資料", HttpStatus.BAD_GATEWAY),

    DATABASE_ERROR("9998", "資料庫操作失敗", HttpStatus.INTERNAL_SERVER_ERROR),
    SYSTEM_ERROR("9999", "系統忙碌中，請稍後再試", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ResultCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
