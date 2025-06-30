package com.example.bank.service;

import com.example.bank.dto.TransactionDto;
import com.example.bank.dto.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionDto transactionDto);
    TransactionResponse updateTransaction(UUID id, TransactionDto transactionDto);
    void deleteTransaction(UUID id);
    TransactionResponse getTransactionById(UUID id);
    List<TransactionResponse> getAllTransactions();
    Page<TransactionResponse> getAllTransactions(Pageable pageable);
    List<TransactionResponse> getTransactionsByType(String type);
    List<TransactionResponse> getTransactionsByCategory(String category);
}