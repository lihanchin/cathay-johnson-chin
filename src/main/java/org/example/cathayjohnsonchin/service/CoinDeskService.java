package org.example.cathayjohnsonchin.service;

import org.example.cathayjohnsonchin.dto.coinDesk.CurrencyInfo;
import org.example.cathayjohnsonchin.dto.coinDesk.CurrencyTransformedInfo;
import org.example.cathayjohnsonchin.dto.coinDesk.RawDataResponse;
import org.example.cathayjohnsonchin.dto.coinDesk.TransformedDataResponse;
import org.example.cathayjohnsonchin.dto.currency.CurrencyDto;
import org.example.cathayjohnsonchin.exception.ApiException;
import org.example.cathayjohnsonchin.model.ResultCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CoinDeskService {

    private final RestTemplate restTemplate;
    private final CurrencyService currencyService;
    private final String COINDESK_URL = "https://kengp.github.io/blog/coindesk.json";

    public CoinDeskService(RestTemplate restTemplate, CurrencyService currencyService) {
        this.restTemplate = restTemplate;
        this.currencyService = currencyService;
    }

    public TransformedDataResponse getTransformedData() {
        RawDataResponse raw = this.getRawData();

        Map<String, String> codeLabelMap = currencyService.findByAll()
                .stream()
                .collect(Collectors.toMap(x -> x.getCode().toLowerCase(), CurrencyDto::getChineseName));

        Map<String, CurrencyTransformedInfo> transformedData = raw.getBpi().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            CurrencyInfo value = entry.getValue();

                            return CurrencyTransformedInfo
                                    .builder()
                                    .code(value.getCode())
                                    .chineseName(codeLabelMap.get(value.getCode().toLowerCase()))
                                    .rate(value.getRate())
                                    .rateFloat(value.getRateFloat())
                                    .build();
                        }
                ));

        return TransformedDataResponse.builder()
                .updateTimeUtc(raw.getTime().getUpdatedISO())
                .currenciesMap(transformedData)
                .build();
    }

    public RawDataResponse getRawData() {
        try {
            return restTemplate.getForObject(COINDESK_URL, RawDataResponse.class);
        } catch (RestClientException e) {
            throw new ApiException(ResultCode.EXTERNAL_API_ERROR, e);
        }
    }
}
