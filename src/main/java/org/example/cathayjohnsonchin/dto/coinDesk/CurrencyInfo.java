package org.example.cathayjohnsonchin.dto.coinDesk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CurrencyInfo {

    private String code;

    private String symbol;

    private String rate;

    private String description;

    @JsonProperty("rate_float")
    private BigDecimal rateFloat;
}
