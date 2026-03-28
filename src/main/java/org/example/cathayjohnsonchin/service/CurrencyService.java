package org.example.cathayjohnsonchin.service;

import org.example.cathayjohnsonchin.dto.currency.*;
import org.example.cathayjohnsonchin.entity.Currency;
import org.example.cathayjohnsonchin.exception.ApiException;
import org.example.cathayjohnsonchin.model.ResultCode;
import org.example.cathayjohnsonchin.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public CurrencyDto create(CurrencyCreateRequest request) {
        if (currencyRepository.existsByCode(request.getCode())) {
            throw new ApiException(ResultCode.DUPLICATE_DATA);
        }

        try {
            Currency currency = new Currency();
            currency.setCode(request.getCode());
            currency.setChineseName(request.getChineseName());
            currency = currencyRepository.save(currency);
            return toDto(currency);
        } catch (Exception e) {
            throw new ApiException(ResultCode.DATABASE_ERROR, e);
        }
    }

    public CurrencyDto findById(Integer id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ApiException(ResultCode.NOT_FOUND));

        return toDto(currency);
    }

    public CurrencyDto findByCode(String code) {
        Currency currency = currencyRepository.findCurrencyByCode(code)
                .orElseThrow(() -> new ApiException(ResultCode.NOT_FOUND));

        return toDto(currency);
    }

    public List<CurrencyDto> findByAll() {
        return currencyRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public CurrencyDto update(Integer id, CurrencyUpdateRequest request) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ApiException(ResultCode.NOT_FOUND));

        if (request.getCode() != null) {
            currency.setCode(request.getCode());
        }

        if (request.getChineseName() != null) {
            currency.setChineseName(request.getChineseName());
        }

        try {
            currencyRepository.save(currency);

            return toDto(currency);
        } catch (Exception e) {
            throw new ApiException(ResultCode.DATABASE_ERROR, e);
        }
    }

    public void delete(Integer id) {
        try {
            currencyRepository.deleteById(id);
        } catch (Exception e) {
            throw new ApiException(ResultCode.DATABASE_ERROR, e);
        }
    }

    private CurrencyDto toDto(Currency currency) {
        return CurrencyDto.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .chineseName(currency.getChineseName())
                .build();
    }
}
