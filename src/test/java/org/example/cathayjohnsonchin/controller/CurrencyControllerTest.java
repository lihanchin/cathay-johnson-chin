package org.example.cathayjohnsonchin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cathayjohnsonchin.dto.currency.CurrencyCreateRequest;
import org.example.cathayjohnsonchin.dto.currency.CurrencyDto;
import org.example.cathayjohnsonchin.dto.currency.CurrencyUpdateRequest;
import org.example.cathayjohnsonchin.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrencyService currencyService;

    private CurrencyDto currencyDto;

    @BeforeEach
    void setup() {
        currencyDto = CurrencyDto.builder()
                .id(1)
                .code("USD")
                .chineseName("美元")
                .build();
    }

    @Test
    void getAll_shouldReturnAllCurrencies() throws Exception {
        given(currencyService.findByAll())
                .willReturn(List.of(currencyDto));

        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencies[0].code").value("USD"))
                .andExpect(jsonPath("$.currencies[0].chineseName").value("美元"));

        verify(currencyService).findByAll();
    }

    @Test
    void getById_shouldReturnCurrencyById() throws Exception {
        int id = 1;

        given(currencyService.findById(id))
                .willReturn(currencyDto);

        mockMvc.perform(get("/api/currencies/id/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency.code").value("USD"))
                .andExpect(jsonPath("$.currency.chineseName").value("美元"));

        verify(currencyService).findById(id);
    }

    @Test
    void getByCode_shouldReturnCurrencyByCode() throws Exception {
        String code = "USD";

        given(currencyService.findByCode(code))
                .willReturn(currencyDto);

        mockMvc.perform(get("/api/currencies/code/{code}", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency.code").value("USD"))
                .andExpect(jsonPath("$.currency.chineseName").value("美元"));

        verify(currencyService).findByCode(code);
    }

    @Test
    void create_shouldReturnCurrencyWhenSuccess() throws Exception {
        CurrencyCreateRequest request = new CurrencyCreateRequest("eur", "歐元");

        currencyDto.setId(2);
        currencyDto.setCode("EUR");
        currencyDto.setChineseName("歐元");

        given(currencyService.create(argThat(req ->
                        "eur".equals(req.getCode()) &&
                                "歐元".equals(req.getChineseName())
                )
        )).willReturn(currencyDto);

        mockMvc.perform(post("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency.code").value("EUR"))
                .andExpect(jsonPath("$.currency.chineseName").value("歐元"));

        verify(currencyService).create(request);
    }

    @Test
    void update_shouldReturnCurrencyWhenSuccess() throws Exception {
        int id = 2;

        CurrencyUpdateRequest request = new CurrencyUpdateRequest("gbp", "英鎊");

        currencyDto.setId(id);
        currencyDto.setCode("GBP");
        currencyDto.setChineseName("英鎊");

        given(currencyService.update(eq(id), argThat(req ->
                        "gbp".equals(req.getCode()) &&
                                "英鎊".equals(req.getChineseName())
                )
        )).willReturn(currencyDto);

        mockMvc.perform(put("/api/currencies/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency.code").value("GBP"))
                .andExpect(jsonPath("$.currency.chineseName").value("英鎊"));

        verify(currencyService).update(id, request);
    }

    @Test
    void delete_shouldReturnNoContentWhenSuccess() throws Exception {
        int id = 1;

        mockMvc.perform(delete("/api/currencies/id/{id}", id))
                .andExpect(status().isNoContent());

        verify(currencyService).delete(id);
    }
}
