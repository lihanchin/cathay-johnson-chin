package org.example.cathayjohnsonchin.dto.currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyUpdateRequest {

    private String code;

    private String chineseName;
}
