package org.example.cathayjohnsonchin.exception;

import lombok.Getter;
import org.example.cathayjohnsonchin.model.ResultCode;

@Getter
public class ApiException extends RuntimeException {
    private final ResultCode resultCode;

    public ApiException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ApiException(ResultCode resultCode, String customMessage) {
        super(customMessage);
        this.resultCode = resultCode;
    }

    public ApiException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
    }
}