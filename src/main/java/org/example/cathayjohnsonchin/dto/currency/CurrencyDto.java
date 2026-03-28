package org.example.cathayjohnsonchin.dto.currency;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyDto {

    private Integer id;

    private String code;

    private String chineseName;
}
