package com.example.bank.controller;

import com.example.bank.dto.TransactionDto;
import com.example.bank.dto.TransactionResponse;
import com.example.bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionDto transactionDto;
    private TransactionResponse transactionResponse;
    private UUID transactionId;

    @BeforeEach
    void setUp() {
        transactionId = UUID.randomUUID();

        transactionDto = new TransactionDto();
        transactionDto.setDescription("Salary Deposit");
        transactionDto.setAmount(new BigDecimal("1500.00"));
        transactionDto.setType("CREDIT");
        transactionDto.setCategory("Salary");

        transactionResponse = new TransactionResponse();
        transactionResponse.setId(transactionId);
        transactionResponse.setDescription("Salary Deposit");
        transactionResponse.setAmount(new BigDecimal("1500.00"));
        transactionResponse.setType("CREDIT");
        transactionResponse.setCategory("Salary");
    }

    @Test
    void createTransaction_WithValidData_ShouldReturnCreated() {
        when(transactionService.createTransaction(any(TransactionDto.class)))
                .thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response =
                transactionController.createTransaction(transactionDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(transactionId, response.getBody().getId());
        verify(transactionService, times(1)).createTransaction(any(TransactionDto.class));
    }

    @Test
    void updateTransaction_WithExistingId_ShouldReturnOk() {
        when(transactionService.updateTransaction(eq(transactionId), any(TransactionDto.class)))
                .thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response =
                transactionController.updateTransaction(transactionId, transactionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(transactionId, response.getBody().getId());
        verify(transactionService, times(1))
                .updateTransaction(eq(transactionId), any(TransactionDto.class));
    }

    @Test
    void deleteTransaction_WithExistingId_ShouldReturnNoContent() {
        doNothing().when(transactionService).deleteTransaction(transactionId);

        ResponseEntity<Void> response =
                transactionController.deleteTransaction(transactionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }

    @Test
    void getTransactionById_WithExistingId_ShouldReturnOk() {
        when(transactionService.getTransactionById(transactionId))
                .thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response =
                transactionController.getTransactionById(transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(transactionId, response.getBody().getId());
        verify(transactionService, times(1)).getTransactionById(transactionId);
    }

    @Test
    void getAllTransactions_ShouldReturnOk() {
        List<TransactionResponse> responses = Collections.singletonList(transactionResponse);
        when(transactionService.getAllTransactions()).thenReturn(responses);

        ResponseEntity<List<TransactionResponse>> response =
                transactionController.getAllTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(transactionService, times(1)).getAllTransactions();
    }

    @Test
    void getAllTransactionsPaged_ShouldReturnOk() {
        Page<TransactionResponse> pagedResponse = new PageImpl<>(
                Collections.singletonList(transactionResponse)
        );
        when(transactionService.getAllTransactions(any())).thenReturn(pagedResponse);

        ResponseEntity<Page<TransactionResponse>> response =
                transactionController.getAllTransactionsPaged(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(transactionService, times(1)).getAllTransactions(any());
    }

    @Test
    void getTransactionsByType_ShouldReturnOk() {
        List<TransactionResponse> responses = Collections.singletonList(transactionResponse);
        when(transactionService.getTransactionsByType("CREDIT")).thenReturn(responses);

        ResponseEntity<List<TransactionResponse>> response =
                transactionController.getTransactionsByType("CREDIT");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(transactionService, times(1)).getTransactionsByType("CREDIT");
    }

    @Test
    void getTransactionsByCategory_ShouldReturnOk() {
        List<TransactionResponse> responses = Collections.singletonList(transactionResponse);
        when(transactionService.getTransactionsByCategory("Salary")).thenReturn(responses);

        ResponseEntity<List<TransactionResponse>> response =
                transactionController.getTransactionsByCategory("Salary");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(transactionService, times(1)).getTransactionsByCategory("Salary");
    }

    @Test
    void getAllTransactions_WithEmptyResult_ShouldReturnOk() {
        when(transactionService.getAllTransactions()).thenReturn(Collections.emptyList());

        ResponseEntity<List<TransactionResponse>> response =
                transactionController.getAllTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getTransactionsByType_WithEmptyResult_ShouldReturnOk() {
        when(transactionService.getTransactionsByType("DEBIT")).thenReturn(Collections.emptyList());

        ResponseEntity<List<TransactionResponse>> response =
                transactionController.getTransactionsByType("DEBIT");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getAllTransactionsPaged_WithEmptyResult_ShouldReturnOk() {
        Page<TransactionResponse> emptyPage = new PageImpl<>(Collections.emptyList());
        when(transactionService.getAllTransactions(any())).thenReturn(emptyPage);

        ResponseEntity<Page<TransactionResponse>> response =
                transactionController.getAllTransactionsPaged(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}