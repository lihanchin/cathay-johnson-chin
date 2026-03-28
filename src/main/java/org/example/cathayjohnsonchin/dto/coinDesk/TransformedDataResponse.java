package org.example.cathayjohnsonchin.dto.coinDesk;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
@Schema(description = "轉換後的 CoinDesk 資料回應")
public class TransformedDataResponse {

    @Schema(description = "更新時間 (格式: yyyy/MM/dd HH:mm:ss)", example = "2024/09/02 07:07:20")
    private String updateTimeUtc;

    @Schema(description = "幣別資訊 Map，Key 為幣別代碼")
    private Map<String, CurrencyTransformedInfo> currenciesMap;
}
