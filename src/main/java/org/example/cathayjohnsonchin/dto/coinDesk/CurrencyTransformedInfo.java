package org.example.cathayjohnsonchin.dto.coinDesk;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CurrencyTransformedInfo {

    private String code;

    private String chineseName;

    private String rate;

    private BigDecimal rateFloat;
}
