package org.example.cathayjohnsonchin.dto.coinDesk;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class RawDataResponse {

    private TimeInfo time;

    private String disclaimer;

    private String chartName;

    private Map<String, CurrencyInfo> bpi;

    @Data
    @AllArgsConstructor
    public static class TimeInfo {

        private String updated;

        private String updatedISO;

        private String updateduk;
    }
}
