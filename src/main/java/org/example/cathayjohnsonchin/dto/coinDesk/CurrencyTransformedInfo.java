package org.example.cathayjohnsonchin.dto.coinDesk;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "轉換後的幣別資訊")
public class CurrencyTransformedInfo {

    @Schema(description = "幣別代碼", example = "USD")
    private String code;

    @Schema(description = "幣別中文名稱", example = "美元")
    private String chineseName;

    @Schema(description = "匯率 (字串格式)", example = "57,756.298")
    private String rate;

    @Schema(description = "匯率 (數值格式)", example = "57756.2984")
    private BigDecimal rateFloat;
}
