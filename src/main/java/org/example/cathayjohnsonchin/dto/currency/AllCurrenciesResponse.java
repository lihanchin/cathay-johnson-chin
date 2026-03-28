package org.example.cathayjohnsonchin.dto.currency;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllCurrenciesResponse {

    private List<CurrencyDto> currencies;
}
