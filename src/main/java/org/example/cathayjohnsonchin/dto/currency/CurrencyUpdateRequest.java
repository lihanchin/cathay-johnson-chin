package org.example.cathayjohnsonchin.dto.currency;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "更新幣別請求")
public class CurrencyUpdateRequest {

    @Schema(description = "幣別代碼", example = "USD")
    private String code;

    @Schema(description = "中文名稱", example = "美元")
    private String chineseName;
}
