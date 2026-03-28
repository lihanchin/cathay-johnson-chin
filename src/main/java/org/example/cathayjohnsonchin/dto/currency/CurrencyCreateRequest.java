package org.example.cathayjohnsonchin.dto.currency;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyCreateRequest {

    @NotBlank(message = "code 不能為空")
    private String code;

    @NotBlank(message = "chineseName 不能為空")
    private String chineseName;
}
