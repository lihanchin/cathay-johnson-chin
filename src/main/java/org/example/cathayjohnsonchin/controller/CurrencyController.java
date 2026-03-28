package org.example.cathayjohnsonchin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.cathayjohnsonchin.dto.currency.*;
import org.example.cathayjohnsonchin.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "幣別管理", description = "幣別維護 API，提供 CRUD 功能")
@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(summary = "查詢所有幣別", description = "從資料庫中獲取所有已存在的幣別列表")
    @GetMapping
    public ResponseEntity<AllCurrenciesResponse> getAll() {
        List<CurrencyDto> list = currencyService.findByAll();

        return ResponseEntity.ok(
                AllCurrenciesResponse.builder()
                .currencies(list)
                .build()
        );
    }

    @Operation(summary = "根據 ID 查詢幣別", description = "輸入唯一的 ID 值來獲取特定幣別的詳細資料")
    @GetMapping("/id/{id}")
    public ResponseEntity<CurrencyResponse> getById(@Parameter(description = "幣別 ID", example = "1") @PathVariable @NotNull Integer id) {
        CurrencyDto currency = currencyService.findById(id);

        return ResponseEntity.ok(
                CurrencyResponse.builder()
                .currency(currency)
                .build()
        );
    }

    @Operation(summary = "根據幣別代碼查詢幣別", description = "輸入幣別代碼 (例如: USD) 來獲取幣別的詳細資料")
    @GetMapping("/code/{code}")
    public ResponseEntity<CurrencyResponse> getByCode(@Parameter(description = "幣別代碼 (不分大小寫)", example = "USD") @PathVariable @NotNull String code) {
        CurrencyDto currency = currencyService.findByCode(code);

        return ResponseEntity.ok(
                CurrencyResponse.builder()
                .currency(currency)
                .build()
        );
    }

    @Operation(summary = "新增幣別資料", description = "建立新的幣別，包含代碼與中文名稱")
    @PostMapping
    public ResponseEntity<CurrencyResponse> create(@Valid @RequestBody CurrencyCreateRequest request) {
        CurrencyDto dto = currencyService.create(request);

        return ResponseEntity.ok(CurrencyResponse.builder()
                .currency(dto)
                .build()
        );
    }

    @Operation(summary = "更新幣別資料", description = "根據 ID 更新現有的幣別資料 (代碼或中文名稱)")
    @PutMapping({"/id/{id}"})
    public ResponseEntity<CurrencyResponse> update(@Parameter(description = "幣別 ID", example = "1") @PathVariable @NotNull Integer id, @RequestBody CurrencyUpdateRequest request) {
        CurrencyDto dto = currencyService.update(id, request);

        return ResponseEntity.ok(CurrencyResponse.builder()
                        .currency(dto).
                        build());
    }

    @Operation(summary = "刪除幣別資料", description = "根據 ID 從資料庫中移除特定的幣別")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "幣別 ID", example = "1") @PathVariable @NotNull Integer id) {
        currencyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
