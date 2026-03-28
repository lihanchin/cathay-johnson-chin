package org.example.cathayjohnsonchin.controller;

import org.example.cathayjohnsonchin.dto.coinDesk.CurrencyInfo;
import org.example.cathayjohnsonchin.dto.coinDesk.CurrencyTransformedInfo;
import org.example.cathayjohnsonchin.dto.coinDesk.RawDataResponse;
import org.example.cathayjohnsonchin.dto.coinDesk.TransformedDataResponse;
import org.example.cathayjohnsonchin.service.CoinDeskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoinDeskController.class)
class CoinDeskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoinDeskService coinDeskService;

    private RawDataResponse.TimeInfo mockTimeInfo;

    private Map<String, CurrencyInfo> mockBpiMap;

    private Map<String, CurrencyTransformedInfo> mockCurrenciesMap;

    @BeforeEach
    void setup() {
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
    void getRawData_shouldReturnRawDataResponse() throws Exception {
        RawDataResponse mockResponse = new RawDataResponse(
                mockTimeInfo,
                "test",
                "test123",
                mockBpiMap);

        given(coinDeskService.getRawData()).willReturn(mockResponse);

        mockMvc.perform(get("/api/coindesk/raw")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chartName").value("test123"))
                .andExpect(jsonPath("$.disclaimer").value("test"));

        verify(coinDeskService).getRawData();
    }

    @Test
    void getTransformedData_shouldReturnTransformedDataResponse() throws Exception {
        TransformedDataResponse mockResponse = TransformedDataResponse.builder()
                .updateTimeUtc("2024-09-02T07:07:20+00:00")
                .currenciesMap(mockCurrenciesMap)
                .build();

        given(coinDeskService.getTransformedData()).willReturn(mockResponse);

        mockMvc.perform(get("/api/coindesk/transformed")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.updateTimeUtc").value("2024-09-02T07:07:20+00:00"))
                .andExpect(jsonPath("$.currenciesMap.USD.code").value("USD"))
                .andExpect(jsonPath("$.currenciesMap.USD.chineseName").value("美金"));

        verify(coinDeskService).getTransformedData();
    }
}
