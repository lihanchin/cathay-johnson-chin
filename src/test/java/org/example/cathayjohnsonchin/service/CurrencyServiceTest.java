package org.example.cathayjohnsonchin.service;

import org.example.cathayjohnsonchin.dto.currency.CurrencyCreateRequest;
import org.example.cathayjohnsonchin.dto.currency.CurrencyDto;
import org.example.cathayjohnsonchin.dto.currency.CurrencyUpdateRequest;
import org.example.cathayjohnsonchin.entity.Currency;
import org.example.cathayjohnsonchin.exception.ApiException;
import org.example.cathayjohnsonchin.model.ResultCode;
import org.example.cathayjohnsonchin.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    private CurrencyCreateRequest request;

    private Currency currency;

    @BeforeEach
    void setup() {
        request = new CurrencyCreateRequest(" USD ", " 美元 ");
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 0, 0);

        currency = new Currency();
        currency.setId(1);
        currency.setCode("usd");
        currency.setChineseName("美元");
        currency.setCreatedAt(now);
        currency.setUpdatedAt(now);
        currency.setCreatedUser("system");
        currency.setUpdatedUser("system");
    }

    @Test
    void create_shouldThrowApiException_whenCodeAlreadyExists() {
        when(currencyRepository.existsByCode("usd")).thenReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> currencyService.create(request));

        assertEquals(ResultCode.DUPLICATE_DATA, ex.getResultCode());
        assertEquals(ResultCode.DUPLICATE_DATA.getMessage(), ex.getMessage());

        verify(currencyRepository).existsByCode("usd");
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void create_shouldSaveCurrencyAndReturnDto_whenSuccess() {
        when(currencyRepository.existsByCode("usd")).thenReturn(false);

        Currency savedCurrency = new Currency();
        savedCurrency.setId(1);
        savedCurrency.setCode("usd");
        savedCurrency.setChineseName("美元");

        when(currencyRepository.save(any(Currency.class))).thenReturn(savedCurrency);

        CurrencyDto result = currencyService.create(request);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("USD", result.getCode());
        assertEquals("美元", result.getChineseName());

        ArgumentCaptor<Currency> captor = ArgumentCaptor.forClass(Currency.class);
        verify(currencyRepository).save(captor.capture());

        Currency actualSaved = captor.getValue();
        assertEquals("usd", actualSaved.getCode());
        assertEquals("美元", actualSaved.getChineseName());

        verify(currencyRepository).existsByCode("usd");
    }

    @Test
    void create_shouldThrowDatabaseError_whenSaveFails() {
        when(currencyRepository.existsByCode("usd")).thenReturn(false);
        when(currencyRepository.save(any(Currency.class)))
                .thenThrow(new RuntimeException("DB error"));

        ApiException ex = assertThrows(ApiException.class, () -> currencyService.create(request));

        assertEquals(ResultCode.DATABASE_ERROR, ex.getResultCode());
        assertEquals(ResultCode.DATABASE_ERROR.getMessage(), ex.getMessage());
        assertNotNull(ex.getCause());
        assertEquals("DB error", ex.getCause().getMessage());

        verify(currencyRepository).existsByCode("usd");
        verify(currencyRepository).save(any(Currency.class));
    }

    @Test
    void findByAll_shouldReturnAllCurrencies_whenSuccess() {
        when(currencyRepository.findAll()).thenReturn(List.of(currency));

        List<CurrencyDto> result = currencyService.findByAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(currencyRepository).findAll();
    }

    @Test
    void findByAll_shouldThrowApiException_whenDatabaseError() {
        when(currencyRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        ApiException ex = assertThrows(ApiException.class, () -> currencyService.findByAll());

        assertEquals(ResultCode.DATABASE_ERROR, ex.getResultCode());
        assertEquals(ResultCode.DATABASE_ERROR.getMessage(), ex.getMessage());
        assertNotNull(ex.getCause());
        assertEquals("DB error", ex.getCause().getMessage());
        verify(currencyRepository).findAll();
    }

    @Test
    void findById_shouldReturnCurrencyById_whenSuccess() {
        int id = 1;
        when(currencyRepository.findById(id)).thenReturn(Optional.of(currency));

        CurrencyDto result = currencyService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("USD", result.getCode());
        assertEquals("美元", result.getChineseName());
        verify(currencyRepository).findById(id);
    }

    @Test
    void findById_shouldThrowApiException_whenCurrencyNotFound() {
        int id = 2;
        when(currencyRepository.findById(id)).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () -> currencyService.findById(id));

        assertEquals(ResultCode.NOT_FOUND, ex.getResultCode());
        assertEquals(ResultCode.NOT_FOUND.getMessage(), ex.getMessage());
        verify(currencyRepository).findById(id);
    }

    @Test
    void findById_shouldThrowApiException_whenDatabaseError() {
        int id = 1;
        when(currencyRepository.findById(id)).thenThrow(new RuntimeException("DB error"));

        ApiException ex = assertThrows(ApiException.class, () -> currencyService.findById(id));

        assertEquals(ResultCode.DATABASE_ERROR, ex.getResultCode());
        assertEquals(ResultCode.DATABASE_ERROR.getMessage(), ex.getMessage());
        assertNotNull(ex.getCause());
        assertEquals("DB error", ex.getCause().getMessage());
        verify(currencyRepository).findById(id);
    }

    @Test
    void findByCode_shouldReturnCurrencyByCode_whenSuccess() {
        when(currencyRepository.findCurrencyByCode("usd")).thenReturn(Optional.of(currency));

        CurrencyDto result = currencyService.findByCode("USD");
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("USD", result.getCode());
        assertEquals("美元", result.getChineseName());
        verify(currencyRepository).findCurrencyByCode("usd");
    }

    @Test
    void findByCode_shouldThrowApiException_whenCurrencyNotFound() {
        when(currencyRepository.findCurrencyByCode("ntd")).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () -> currencyService.findByCode("NTD"));

        assertEquals(ResultCode.NOT_FOUND, ex.getResultCode());
        assertEquals(ResultCode.NOT_FOUND.getMessage(), ex.getMessage());
        verify(currencyRepository).findCurrencyByCode("ntd");
    }

    @Test
    void findByCode_shouldThrowApiException_whenDatabaseError() {
        when(currencyRepository.findCurrencyByCode("usd")).thenThrow(new RuntimeException("DB error"));

        ApiException ex = assertThrows(ApiException.class, () -> currencyService.findByCode("USD"));

        assertEquals(ResultCode.DATABASE_ERROR, ex.getResultCode());
        assertEquals(ResultCode.DATABASE_ERROR.getMessage(), ex.getMessage());
        assertNotNull(ex.getCause());
        assertEquals("DB error", ex.getCause().getMessage());
        verify(currencyRepository).findCurrencyByCode("usd");
    }

    @Test
    void update_shouldReturnUpdatedCurrencyDto_whenSuccess() {
        int id = 1;

        CurrencyUpdateRequest request = new CurrencyUpdateRequest("gbp", "英鎊");

        Currency newCurrency = currency;
        newCurrency.setCode("gbp");
        newCurrency.setChineseName("英鎊");

        when(currencyRepository.findById(id)).thenReturn(Optional.of(currency));
        when(currencyRepository.save(any(Currency.class))).thenReturn(newCurrency);

        CurrencyDto result = currencyService.update(id, request);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("GBP", result.getCode());
        assertEquals("英鎊", result.getChineseName());

        ArgumentCaptor<Currency> captor = ArgumentCaptor.forClass(Currency.class);
        verify(currencyRepository).save(captor.capture());

        Currency savedCurrency = captor.getValue();
        assertEquals("gbp", savedCurrency.getCode());
        assertEquals("英鎊", savedCurrency.getChineseName());

        verify(currencyRepository).findById(id);
    }

    @Test
    void update_shouldThrowNotFound_whenCurrencyDoesNotExist() {
        int id = 3;
        CurrencyUpdateRequest request = new CurrencyUpdateRequest("gbp", "英鎊");

        when(currencyRepository.findById(id)).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class,
                () -> currencyService.update(id, request));

        assertEquals(ResultCode.NOT_FOUND, ex.getResultCode());

        verify(currencyRepository).findById(id);
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void update_shouldThrowDatabaseError_whenSaveFails() {
        Integer id = 1;
        CurrencyUpdateRequest request = new CurrencyUpdateRequest("gbp", "英鎊");

        when(currencyRepository.findById(id)).thenReturn(Optional.of(currency));
        when(currencyRepository.save(any(Currency.class)))
                .thenThrow(new RuntimeException("DB error"));

        ApiException ex = assertThrows(ApiException.class,
                () -> currencyService.update(id, request));

        assertEquals(ResultCode.DATABASE_ERROR, ex.getResultCode());
        assertNotNull(ex.getCause());
        assertEquals("DB error", ex.getCause().getMessage());

        verify(currencyRepository).findById(id);
        verify(currencyRepository).save(any(Currency.class));
    }

    @Test
    void update_shouldOnlyUpdateChineseName_whenCodeIsNull() {
        int id = 1;
        CurrencyUpdateRequest request = new CurrencyUpdateRequest(null, "美金");

        Currency newCurrency = currency;
        newCurrency.setChineseName("美金");

        when(currencyRepository.findById(id)).thenReturn(Optional.of(currency));
        when(currencyRepository.save(any(Currency.class))).thenReturn(newCurrency);

        CurrencyDto result = currencyService.update(id, request);

        assertNotNull(result);
        assertEquals("USD", result.getCode());
        assertEquals("美金", result.getChineseName());

        ArgumentCaptor<Currency> captor = ArgumentCaptor.forClass(Currency.class);
        verify(currencyRepository).save(captor.capture());

        Currency savedCurrency = captor.getValue();
        assertEquals("usd", savedCurrency.getCode());
        assertEquals("美金", savedCurrency.getChineseName());
    }

    @Test
    void delete_shouldDeleteCurrency_whenSuccess() {
        int id = 1;

        doNothing().when(currencyRepository).deleteById(id);

        currencyService.delete(id);

        verify(currencyRepository).deleteById(id);
    }

    @Test
    void delete_shouldThrowApiException_whenDatabaseError() {
        int id = 1;

        doThrow(new RuntimeException("DB error")).when(currencyRepository).deleteById(anyInt());

        ApiException ex = assertThrows(ApiException.class, () -> currencyService.delete(id));

        assertEquals(ResultCode.DATABASE_ERROR, ex.getResultCode());
        assertEquals(ResultCode.DATABASE_ERROR.getMessage(), ex.getMessage());
        assertNotNull(ex.getCause());
        assertEquals("DB error", ex.getCause().getMessage());
    }
}
