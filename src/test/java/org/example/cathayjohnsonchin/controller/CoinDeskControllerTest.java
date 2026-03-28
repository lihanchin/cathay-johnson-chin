package org.example.cathayjohnsonchin.controller;

import org.example.cathayjohnsonchin.dto.coinDesk.CurrencyInfo;
import org.example.cathayjohnsonchin.dto.coinDesk.CurrencyTransformedInfo;
import org.example.cathayjohnsonchin.dto.coinDesk.RawDataResponse;
import org.example.cathayjohnsonchin.dto.coinDesk.TransformedDataResponse;
import org.example.cathayjohnsonchin.service.CoinDeskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebFluxTest(CoinDeskController.class)
class CoinDeskControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CoinDeskService coinDeskService;

    private RawDataResponse.TimeInfo mockTimeInfo;

    private Map<String, CurrencyInfo> mockBpiMap;

    private Map<String, CurrencyTransformedInfo> mockCurrenciesMap;

    @BeforeEach
    public void setup() {
        mockTimeInfo = new RawDataResponse.TimeInfo(
                "Sep 2, 2024 07:07:20 UTC",
                "2024-09-02T07:07:20+00:00",
                "Sep 2, 2024 at 08:07 BST");

        mockBpiMap = Map.of("USD",
                new CurrencyInfo(
                        "USD",
                        "&#36;",
                        "57,756.298",
                        "United States Dollar",
                        BigDecimal.valueOf(57756.2984)));

        mockCurrenciesMap = Map.of(
                "USD",
                CurrencyTransformedInfo
                        .builder()
                        .code("USD")
                        .chineseName("美金")
                        .rate("57,756.298")
                        .rateFloat(BigDecimal.valueOf(57756.2984))
                        .build());
    }

    @Test
    void getRawData_shouldReturnRawDataResponse() {
        RawDataResponse mockResponse = new RawDataResponse(
                mockTimeInfo,
                "test",
                "test123",
                mockBpiMap);

        given(coinDeskService.getRawData()).willReturn(Mono.just(mockResponse));

        webTestClient.get()
                .uri("/api/coindesk/raw")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RawDataResponse.class)
                .isEqualTo(mockResponse);

        verify(coinDeskService).getRawData();
    }

    @Test
    void getTransformedData_shouldReturnTransformedDataResponse() {
        TransformedDataResponse mockResponse = TransformedDataResponse.builder()
                .updateTimeUtc("2024-09-02T07:07:20+00:00")
                .currenciesMap(mockCurrenciesMap)
                .build();

        given(coinDeskService.getTransformedData()).willReturn(mockResponse);

        webTestClient.get()
                .uri("/api/coindesk/transformed")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransformedDataResponse.class)
                .isEqualTo(mockResponse);

        verify(coinDeskService).getTransformedData();
    }
}
