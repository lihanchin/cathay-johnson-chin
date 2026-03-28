package org.example.cathayjohnsonchin.dto.coinDesk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class TransformedDataResponse {

    private String updateTimeUtc;

    private Map<String, CurrencyTransformedInfo> currenciesMap;
}
