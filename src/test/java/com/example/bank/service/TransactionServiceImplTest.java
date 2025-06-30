package com.example.bank.service;

import com.example.bank.dto.TransactionDto;
import com.example.bank.dto.TransactionResponse;
import com.example.bank.exception.TransactionNotFoundException;
import com.example.bank.exception.ValidationException;
import com.example.bank.model.Transaction;
import com.example.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionDto validTransactionDto;
    private Transaction sampleTransaction;
    private UUID sampleTransactionId;

    @BeforeEach
    void setUp() {
        sampleTransactionId = UUID.randomUUID();

        validTransactionDto = new TransactionDto();
        validTransactionDto.setDescription("Salary Deposit");
        validTransactionDto.setAmount(new BigDecimal("1500.00"));
        validTransactionDto.setType("CREDIT");
        validTransactionDto.setCategory("Salary");

        sampleTransaction = new Transaction();
        sampleTransaction.setId(sampleTransactionId);
        sampleTransaction.setDescription("Salary Deposit");
        sampleTransaction.setAmount(new BigDecimal("1500.00"));
        sampleTransaction.setType("CREDIT");
        sampleTransaction.setCategory("Salary");
        sampleTransaction.setTimestamp(LocalDateTime.now());
    }

    // ========== 创建交易测试 ==========
    @Test
    void createTransaction_WithValidData_ShouldReturnTransactionResponse() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(sampleTransaction);

        TransactionResponse response = transactionService.createTransaction(validTransactionDto);

        assertNotNull(response);
        assertEquals(sampleTransactionId, response.getId());
        assertEquals("Salary Deposit", response.getDescription());
        assertEquals(new BigDecimal("1500.00"), response.getAmount());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_WithNegativeAmount_ShouldThrowValidationException() {
        validTransactionDto.setAmount(new BigDecimal("-100.00"));

        assertThrows(ValidationException.class, () ->
                transactionService.createTransaction(validTransactionDto)
        );
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void createTransaction_WithZeroAmount_ShouldThrowValidationException() {
        validTransactionDto.setAmount(BigDecimal.ZERO);

        assertThrows(ValidationException.class, () ->
                transactionService.createTransaction(validTransactionDto)
        );
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void createTransaction_WithInvalidType_ShouldThrowValidationException() {
        validTransactionDto.setType("INVALID_TYPE");

        assertThrows(ValidationException.class, () ->
                transactionService.createTransaction(validTransactionDto)
        );
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void createTransaction_WithEmptyDescription_ShouldThrowValidationException() {
        validTransactionDto.setDescription("  ");

        assertThrows(ValidationException.class, () ->
                transactionService.createTransaction(validTransactionDto)
        );
        verify(transactionRepository, never()).save(any());
    }

    // ========== 更新交易测试 ==========
    @Test
    void updateTransaction_WithValidData_ShouldReturnUpdatedTransaction() {
        when(transactionRepository.findById(sampleTransactionId)).thenReturn(Optional.of(sampleTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(sampleTransaction);

        TransactionResponse response = transactionService.updateTransaction(sampleTransactionId, validTransactionDto);

        assertNotNull(response);
        assertEquals(sampleTransactionId, response.getId());
        verify(transactionRepository, times(1)).findById(sampleTransactionId);
        verify(transactionRepository, times(1)).save(sampleTransaction);
    }

    @Test
    void updateTransaction_WithNonExistingId_ShouldThrowTransactionNotFoundException() {
        UUID nonExistingId = UUID.randomUUID();
        when(transactionRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () ->
                transactionService.updateTransaction(nonExistingId, validTransactionDto)
        );
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void updateTransaction_WithInvalidAmount_ShouldThrowValidationException() {
        validTransactionDto.setAmount(new BigDecimal("-50.00"));

        assertThrows(ValidationException.class, () ->
                transactionService.updateTransaction(sampleTransactionId, validTransactionDto)
        );
        verify(transactionRepository, never()).save(any());
    }

    // ========== 删除交易测试 ==========
    @Test
    void deleteTransaction_WithExistingId_ShouldDeleteSuccessfully() {
        when(transactionRepository.existsById(sampleTransactionId)).thenReturn(true);

        transactionService.deleteTransaction(sampleTransactionId);

        verify(transactionRepository, times(1)).existsById(sampleTransactionId);
        verify(transactionRepository, times(1)).deleteById(sampleTransactionId);
    }

    @Test
    void deleteTransaction_WithNonExistingId_ShouldThrowTransactionNotFoundException() {
        UUID nonExistingId = UUID.randomUUID();
        when(transactionRepository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () ->
                transactionService.deleteTransaction(nonExistingId)
        );
        verify(transactionRepository, never()).deleteById(any());
    }

    // ========== 查询交易测试 ==========
    @Test
    void getTransactionById_WithExistingId_ShouldReturnTransaction() {
        when(transactionRepository.findById(sampleTransactionId)).thenReturn(Optional.of(sampleTransaction));

        TransactionResponse response = transactionService.getTransactionById(sampleTransactionId);

        assertNotNull(response);
        assertEquals(sampleTransactionId, response.getId());
        verify(transactionRepository, times(1)).findById(sampleTransactionId);
    }

    @Test
    void getTransactionById_WithNonExistingId_ShouldThrowTransactionNotFoundException() {
        UUID nonExistingId = UUID.randomUUID();
        when(transactionRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () ->
                transactionService.getTransactionById(nonExistingId)
        );
    }

    @Test
    void getAllTransactions_ShouldReturnAllTransactions() {
        List<Transaction> transactions = Collections.singletonList(sampleTransaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<TransactionResponse> responses = transactionService.getAllTransactions();

        assertEquals(1, responses.size());
        assertEquals(sampleTransactionId, responses.get(0).getId());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getAllTransactions_WithEmptyRepository_ShouldReturnEmptyList() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        List<TransactionResponse> responses = transactionService.getAllTransactions();

        assertTrue(responses.isEmpty());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getAllTransactionsPaged_ShouldReturnPagedResults() {
        List<Transaction> transactions = Arrays.asList(
                sampleTransaction,
                new Transaction("Grocery", new BigDecimal("100.00"), "DEBIT", "Shopping")
        );
        when(transactionRepository.findAll()).thenReturn(transactions);
        Pageable pageable = PageRequest.of(0, 1);

        Page<TransactionResponse> page = transactionService.getAllTransactions(pageable);

        assertEquals(2, page.getTotalElements());
        assertEquals(1, page.getContent().size());
    }

    @Test
    void getTransactionsByType_ShouldReturnFilteredResults() {
        Transaction debitTransaction = new Transaction("Grocery", new BigDecimal("50.00"), "DEBIT", "Shopping");
        List<Transaction> transactions = Arrays.asList(sampleTransaction, debitTransaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<TransactionResponse> creditTransactions = transactionService.getTransactionsByType("CREDIT");
        List<TransactionResponse> debitTransactions = transactionService.getTransactionsByType("DEBIT");

        assertEquals(1, creditTransactions.size());
        assertEquals("CREDIT", creditTransactions.get(0).getType());
        assertEquals(1, debitTransactions.size());
        assertEquals("DEBIT", debitTransactions.get(0).getType());
    }

    @Test
    void getTransactionsByCategory_ShouldReturnFilteredResults() {
        Transaction shoppingTransaction = new Transaction("Grocery", new BigDecimal("50.00"), "DEBIT", "Shopping");
        List<Transaction> transactions = Arrays.asList(sampleTransaction, shoppingTransaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<TransactionResponse> salaryTransactions = transactionService.getTransactionsByCategory("Salary");
        List<TransactionResponse> shoppingTransactions = transactionService.getTransactionsByCategory("Shopping");

        assertEquals(1, salaryTransactions.size());
        assertEquals("Salary", salaryTransactions.get(0).getCategory());
        assertEquals(1, shoppingTransactions.size());
        assertEquals("Shopping", shoppingTransactions.get(0).getCategory());
    }

    // ========== 验证方法测试 ==========
    @Test
    void validateTransaction_WithValidData_ShouldNotThrowException() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(sampleTransaction);

        assertDoesNotThrow(() ->
                transactionService.createTransaction(validTransactionDto)
        );
    }

    @Test
    void validateTransaction_WithNullAmount_ShouldThrowValidationException() {
        validTransactionDto.setAmount(null);

        assertThrows(ValidationException.class, () ->
                transactionService.createTransaction(validTransactionDto)
        );
    }

    @Test
    void validateTransaction_WithNullType_ShouldThrowValidationException() {
        validTransactionDto.setType(null);

        assertThrows(ValidationException.class, () ->
                transactionService.createTransaction(validTransactionDto)
        );
    }

    @Test
    void validateTransaction_WithNullDescription_ShouldThrowValidationException() {
        validTransactionDto.setDescription(null);

        assertThrows(ValidationException.class, () ->
                transactionService.createTransaction(validTransactionDto)
        );
    }
}