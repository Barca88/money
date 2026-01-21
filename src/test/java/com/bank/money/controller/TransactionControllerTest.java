package com.bank.money.controller;

import com.bank.money.domain.Transaction;
import com.bank.money.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService service;

    @Test
    void shouldCreateTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("500"));
        transaction.setFee(new BigDecimal("18"));

        when(service.save(any(Transaction.class))).thenReturn(transaction);

        String json = """
        {
          "accountOrigin": "123",
          "accountDestination": "456",
          "scheduleDate": "%s",
          "amount": 500
        }
        """.formatted(LocalDate.now());

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldFailValidation_WhenAmountIsMissing() throws Exception {
        String json = """
        {
          "accountOrigin": "123",
          "accountDestination": "456",
          "scheduleDate": "%s"
        }
        """.formatted(LocalDate.now());

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}
