package org.example.cathayjohnsonchin.controller;

import org.example.cathayjohnsonchin.dto.coinDesk.RawDataResponse;
import org.example.cathayjohnsonchin.dto.coinDesk.TransformedDataResponse;
import org.example.cathayjohnsonchin.service.CoinDeskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/coindesk")
public class CoinDeskController {

    private final CoinDeskService coinDeskService;

    public CoinDeskController(CoinDeskService coinDeskService) {
        this.coinDeskService = coinDeskService;
    }

    @GetMapping("/raw")
    public Mono<RawDataResponse> getRawData() {
        return coinDeskService.getRawData();
    }

    @GetMapping("/transformed")
    public ResponseEntity<TransformedDataResponse> getTransformedData() {
        TransformedDataResponse response = coinDeskService.getTransformedData();
        return ResponseEntity.ok(response);
    }
}
