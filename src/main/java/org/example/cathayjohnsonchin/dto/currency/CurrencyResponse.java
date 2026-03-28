package org.example.cathayjohnsonchin.dto.currency;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyResponse {

    private CurrencyDto currency;
}
