package org.example.cathayjohnsonchin.dto.general;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String resultCode;

    private String resultMessage;
}
