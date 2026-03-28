package org.example.cathayjohnsonchin.service;

import org.example.cathayjohnsonchin.dto.coinDesk.CurrencyInfo;
import org.example.cathayjohnsonchin.dto.coinDesk.CurrencyTransformedInfo;
import org.example.cathayjohnsonchin.dto.coinDesk.RawDataResponse;
import org.example.cathayjohnsonchin.dto.coinDesk.TransformedDataResponse;
import org.example.cathayjohnsonchin.dto.currency.CurrencyDto;
import org.example.cathayjohnsonchin.exception.ApiException;
import org.example.cathayjohnsonchin.model.ResultCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CoinDeskService {

    private final WebClient coinDeskClient;
    private final CurrencyService currencyService;

    public CoinDeskService(@Qualifier("coinDeskClient") WebClient coinDeskClient, CurrencyService currencyService) {
        this.coinDeskClient = coinDeskClient;
        this.currencyService = currencyService;
    }

    public TransformedDataResponse getTransformedData() {
        RawDataResponse raw = this.getRawData()
                .blockOptional()
                .orElseThrow(() -> new ApiException(ResultCode.EXTERNAL_API_NO_DATA));

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

    public Mono<RawDataResponse> getRawData() {
        return coinDeskClient.get()
                .uri("/blog/coindesk.json")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RawDataResponse.class);
    }
}
