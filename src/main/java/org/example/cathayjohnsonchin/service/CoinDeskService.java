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

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CoinDeskService {

    private final RestTemplate restTemplate;
    private final CurrencyService currencyService;
    private final String COINDESK_URL = "https://kengp3.github.io/blog/coindesk.json";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

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

        String formattedTime = "";
        try {
            OffsetDateTime odt = OffsetDateTime.parse(raw.getTime().getUpdatedISO());
            formattedTime = odt.format(FORMATTER);
        } catch (Exception e) {
            formattedTime = raw.getTime().getUpdatedISO();
        }

        return TransformedDataResponse.builder()
                .updateTimeUtc(formattedTime)
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
