package org.example.cathayjohnsonchin.dto.currency;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "新增幣別請求")
public class CurrencyCreateRequest {

    @NotBlank(message = "code 不能為空")
    @Schema(description = "幣別代碼", example = "JPY")
    private String code;

    @NotBlank(message = "chineseName 不能為空")
    @Schema(description = "中文名稱", example = "日圓")
    private String chineseName;
}
