package org.example.cathayjohnsonchin.service;

import org.example.cathayjohnsonchin.dto.coinDesk.CurrencyInfo;
import org.example.cathayjohnsonchin.dto.coinDesk.RawDataResponse;
import org.example.cathayjohnsonchin.dto.coinDesk.TransformedDataResponse;
import org.example.cathayjohnsonchin.dto.currency.CurrencyDto;
import org.example.cathayjohnsonchin.exception.ApiException;
import org.example.cathayjohnsonchin.model.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoinDeskServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CoinDeskService coinDeskService;

    private RawDataResponse mockRawData;
    private List<CurrencyDto> mockCurrencies;

    @BeforeEach
    void setUp() {
        RawDataResponse.TimeInfo timeInfo = new RawDataResponse.TimeInfo(
                "Sep 2, 2024 07:07:20 UTC",
                "2024-09-02T07:07:20+00:00",
                "Sep 2, 2024 at 08:07 BST"
        );

        Map<String, CurrencyInfo> bpi = Map.of(
                "USD", new CurrencyInfo("USD", "$", "50,000.00", "United States Dollar", new BigDecimal("50000.0")),
                "GBP", new CurrencyInfo("GBP", "£", "40,000.00", "British Pound Sterling", new BigDecimal("40000.0")),
                "EUR", new CurrencyInfo("EUR", "€", "45,000.00", "Euro", new BigDecimal("45000.0"))
        );

        mockRawData = new RawDataResponse(timeInfo, "Disclaimer", "Bitcoin", bpi);

        mockCurrencies = List.of(
                CurrencyDto.builder().id(1).code("USD").chineseName("美金").build(),
                CurrencyDto.builder().id(2).code("GBP").chineseName("英鎊").build()
        );
    }

    @Test
    void getRawData_shouldReturnRawDataResponse() {
        when(restTemplate.getForObject(anyString(), eq(RawDataResponse.class))).thenReturn(mockRawData);

        RawDataResponse result = coinDeskService.getRawData();

        assertNotNull(result);
        assertEquals("Bitcoin", result.getChartName());
        assertEquals(3, result.getBpi().size());
        verify(restTemplate).getForObject(anyString(), eq(RawDataResponse.class));
    }

    @Test
    void getRawData_shouldThrowApiException_whenRestClientExceptionOccurs() {
        when(restTemplate.getForObject(anyString(), eq(RawDataResponse.class)))
                .thenThrow(new RestClientException("Connection failed"));

        ApiException exception = assertThrows(ApiException.class, () -> coinDeskService.getRawData());

        assertEquals(ResultCode.EXTERNAL_API_ERROR, exception.getResultCode());
        assertEquals("Connection failed", exception.getCause().getMessage());
        verify(restTemplate).getForObject(anyString(), eq(RawDataResponse.class));
    }

    @Test
    void getTransformedData_shouldTransformCorrectly() {
        when(restTemplate.getForObject(anyString(), eq(RawDataResponse.class))).thenReturn(mockRawData);
        when(currencyService.findByAll()).thenReturn(mockCurrencies);

        TransformedDataResponse result = coinDeskService.getTransformedData();

        assertNotNull(result);
        assertEquals("2024/09/02 07:07:20", result.getUpdateTimeUtc());
        assertEquals(3, result.getCurrenciesMap().size());

        assertTrue(result.getCurrenciesMap().containsKey("USD"));
        assertEquals("USD", result.getCurrenciesMap().get("USD").getCode());
        assertEquals("美金", result.getCurrenciesMap().get("USD").getChineseName());

        assertTrue(result.getCurrenciesMap().containsKey("GBP"));
        assertEquals("GBP", result.getCurrenciesMap().get("GBP").getCode());
        assertEquals("英鎊", result.getCurrenciesMap().get("GBP").getChineseName());
    }

    @Test
    void getTransformedData_shouldChineseNameIsNull_whenCodeLabelMapIsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(RawDataResponse.class))).thenReturn(mockRawData);
        when(currencyService.findByAll()).thenReturn(List.of());

        TransformedDataResponse result = coinDeskService.getTransformedData();

        assertNotNull(result);
        assertEquals("2024/09/02 07:07:20", result.getUpdateTimeUtc());
        assertEquals(3, result.getCurrenciesMap().size());

        assertTrue(result.getCurrenciesMap().containsKey("USD"));
        assertEquals("USD", result.getCurrenciesMap().get("USD").getCode());
        assertNull(result.getCurrenciesMap().get("USD").getChineseName());

        assertTrue(result.getCurrenciesMap().containsKey("GBP"));
        assertEquals("GBP", result.getCurrenciesMap().get("GBP").getCode());
        assertNull(result.getCurrenciesMap().get("GBP").getChineseName());
    }

    @Test
    void getTransformedData_shouldFallbackToOriginalTime_whenDateFormatIsInvalid() {
        RawDataResponse.TimeInfo invalidTimeInfo = new RawDataResponse.TimeInfo(
                "Invalid Time",
                "not-an-iso-date",
                "Invalid Time uk"
        );
        RawDataResponse invalidTimeRawData = new RawDataResponse(invalidTimeInfo, "Disclaimer", "Bitcoin", Map.of());

        when(restTemplate.getForObject(anyString(), eq(RawDataResponse.class))).thenReturn(invalidTimeRawData);
        when(currencyService.findByAll()).thenReturn(List.of());

        TransformedDataResponse result = coinDeskService.getTransformedData();

        assertNotNull(result);

        assertEquals("not-an-iso-date", result.getUpdateTimeUtc());
    }

    @Test
    void getTransformedData_shouldHandleDifferentIsoFormats() {
        RawDataResponse.TimeInfo isoWithZTimeInfo = new RawDataResponse.TimeInfo(
                "Sep 2, 2024 07:07:20 UTC",
                "2024-09-02T07:07:20Z",
                "Sep 2, 2024 at 08:07 BST"
        );
        RawDataResponse isoWithZRawData = new RawDataResponse(isoWithZTimeInfo, "Disclaimer", "Bitcoin", Map.of());

        when(restTemplate.getForObject(anyString(), eq(RawDataResponse.class))).thenReturn(isoWithZRawData);
        when(currencyService.findByAll()).thenReturn(List.of());

        TransformedDataResponse result = coinDeskService.getTransformedData();

        assertNotNull(result);
        assertEquals("2024/09/02 07:07:20", result.getUpdateTimeUtc());
    }
}
