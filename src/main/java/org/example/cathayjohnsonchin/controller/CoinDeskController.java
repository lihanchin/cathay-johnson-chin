package org.example.cathayjohnsonchin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.cathayjohnsonchin.dto.coinDesk.RawDataResponse;
import org.example.cathayjohnsonchin.dto.coinDesk.TransformedDataResponse;
import org.example.cathayjohnsonchin.service.CoinDeskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "呼叫 coindesk 的 API", description = "串接 CoinDesk API 並提供匯率資訊")
@RestController
@RequestMapping("/api/coindesk")
public class CoinDeskController {

    private final CoinDeskService coinDeskService;

    public CoinDeskController(CoinDeskService coinDeskService) {
        this.coinDeskService = coinDeskService;
    }

    @Operation(summary = "獲取 CoinDesk 原始資料", description = "直接轉發來自 CoinDesk 的 JSON 回應，不進行任何格式調整")
    @GetMapping("/raw")
    public RawDataResponse getRawData() {
        return coinDeskService.getRawData();
    }

    @Operation(summary = "獲取轉換後的匯率資料", description = "轉換 CoinDesk 原始資料，對應資料庫中的幣別中文名稱並整理更新時間格式")
    @GetMapping("/transformed")
    public TransformedDataResponse getTransformedData() {
        return coinDeskService.getTransformedData();
    }
}
