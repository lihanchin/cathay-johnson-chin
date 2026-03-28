package org.example.cathayjohnsonchin.controller;

import org.example.cathayjohnsonchin.dto.coinDesk.RawDataResponse;
import org.example.cathayjohnsonchin.dto.coinDesk.TransformedDataResponse;
import org.example.cathayjohnsonchin.service.CoinDeskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coindesk")
public class CoinDeskController {

    private final CoinDeskService coinDeskService;

    public CoinDeskController(CoinDeskService coinDeskService) {
        this.coinDeskService = coinDeskService;
    }

    @GetMapping("/raw")
    public RawDataResponse getRawData() {
        return coinDeskService.getRawData();
    }

    @GetMapping("/transformed")
    public TransformedDataResponse getTransformedData() {
        return coinDeskService.getTransformedData();
    }
}
