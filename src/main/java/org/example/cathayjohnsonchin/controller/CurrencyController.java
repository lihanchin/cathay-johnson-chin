package org.example.cathayjohnsonchin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.cathayjohnsonchin.dto.currency.*;
import org.example.cathayjohnsonchin.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<AllCurrenciesResponse> getAll() {
        List<CurrencyDto> list = currencyService.findByAll();

        return ResponseEntity.ok(
                AllCurrenciesResponse.builder()
                .currencies(list)
                .build()
        );
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CurrencyResponse> getById(@PathVariable @NotNull Integer id) {
        CurrencyDto currency = currencyService.findById(id);

        return ResponseEntity.ok(
                CurrencyResponse.builder()
                .currency(currency)
                .build()
        );
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CurrencyResponse> getByCode(@PathVariable @NotNull String code) {
        CurrencyDto currency = currencyService.findByCode(code);

        return ResponseEntity.ok(
                CurrencyResponse.builder()
                .currency(currency)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<CurrencyResponse> create(@Valid @RequestBody CurrencyCreateRequest request) {
        CurrencyDto dto = currencyService.create(request);

        return ResponseEntity.ok(CurrencyResponse.builder()
                .currency(dto)
                .build()
        );
    }

    @PutMapping({"/id/{id}"})
    public ResponseEntity<CurrencyResponse> update(@PathVariable @NotNull Integer id, @RequestBody CurrencyUpdateRequest request) {
        CurrencyDto dto = currencyService.update(id, request);

        return ResponseEntity.ok(CurrencyResponse.builder()
                        .currency(dto).
                        build());
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Integer id) {
        currencyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
