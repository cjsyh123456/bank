package com.example.bank.service;

import com.example.bank.dto.TransactionDto;
import com.example.bank.dto.TransactionResponse;
import com.example.bank.exception.TransactionNotFoundException;
import com.example.bank.exception.ValidationException;
import com.example.bank.model.Transaction;
import com.example.bank.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        logger.info("TransactionService initialized with repository: {}",
                transactionRepository.getClass().getSimpleName());
    }

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public TransactionResponse createTransaction(TransactionDto transactionDto) {
        logger.debug("Attempting to create transaction: {}", transactionDto);
        validateTransaction(transactionDto);

        Transaction transaction = new Transaction(
                transactionDto.getDescription(),
                transactionDto.getAmount(),
                transactionDto.getType(),
                transactionDto.getCategory()
        );

        Transaction savedTransaction = transactionRepository.save(transaction);
        logger.info("Transaction created successfully with ID: {}", savedTransaction.getId());
        return convertToResponse(savedTransaction);
    }

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public TransactionResponse updateTransaction(UUID id, TransactionDto transactionDto) {
        logger.debug("Attempting to update transaction with ID: {}", id);
        validateTransaction(transactionDto);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Transaction not found for update with ID: {}", id);
                    return new TransactionNotFoundException("Transaction not found with id: " + id);
                });

        transaction.setDescription(transactionDto.getDescription());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setType(transactionDto.getType());
        transaction.setCategory(transactionDto.getCategory());

        Transaction updatedTransaction = transactionRepository.save(transaction);
        logger.info("Transaction updated successfully with ID: {}", id);
        return convertToResponse(updatedTransaction);
    }

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public void deleteTransaction(UUID id) {
        logger.debug("Attempting to delete transaction with ID: {}", id);
        if (!transactionRepository.existsById(id)) {
            logger.error("Transaction not found for deletion with ID: {}", id);
            throw new TransactionNotFoundException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
        logger.info("Transaction deleted successfully with ID: {}", id);
    }

    @Override
    @Cacheable(value = "transaction", key = "#id")
    public TransactionResponse getTransactionById(UUID id) {
        logger.debug("Fetching transaction with ID: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Transaction not found with ID: {}", id);
                    return new TransactionNotFoundException("Transaction not found with id: " + id);
                });
        logger.debug("Successfully retrieved transaction with ID: {}", id);
        return convertToResponse(transaction);
    }

    @Override
    @Cacheable(value = "transactions")
    public List<TransactionResponse> getAllTransactions() {
        logger.debug("Fetching all transactions");
        List<TransactionResponse> transactions = transactionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        logger.info("Retrieved {} transactions", transactions.size());
        return transactions;
    }

    @Override
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        logger.debug("Fetching paginated transactions with pageable: {}", pageable);
        List<TransactionResponse> allTransactions = getAllTransactions();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allTransactions.size());

        Page<TransactionResponse> page = new PageImpl<>(
                allTransactions.subList(start, end),
                pageable,
                allTransactions.size()
        );
        logger.debug("Returning page {} of {} with {} items",
                pageable.getPageNumber(), page.getTotalPages(), page.getNumberOfElements());
        return page;
    }

    @Override
    @Cacheable(value = "transactions", key = "#type")
    public List<TransactionResponse> getTransactionsByType(String type) {
        logger.debug("Fetching transactions by type: {}", type);
        List<TransactionResponse> transactions = transactionRepository.findAll().stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        logger.info("Found {} transactions of type: {}", transactions.size(), type);
        return transactions;
    }

    @Override
    @Cacheable(value = "transactions", key = "#category")
    public List<TransactionResponse> getTransactionsByCategory(String category) {
        logger.debug("Fetching transactions by category: {}", category);
        List<TransactionResponse> transactions = transactionRepository.findAll().stream()
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        logger.info("Found {} transactions in category: {}", transactions.size(), category);
        return transactions;
    }

    private TransactionResponse convertToResponse(Transaction transaction) {
        logger.trace("Converting transaction to response for ID: {}", transaction.getId());
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setDescription(transaction.getDescription());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setTimestamp(transaction.getTimestamp());
        response.setCategory(transaction.getCategory());
        return response;
    }

    private void validateTransaction(TransactionDto transactionDto) {
        logger.trace("Validating transaction DTO");
        if (transactionDto.getAmount() == null || transactionDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Validation failed: Amount must be greater than zero");
            throw new ValidationException("Amount must be greater than zero");
        }

        if (transactionDto.getType() == null ||
                (!transactionDto.getType().equalsIgnoreCase("CREDIT") &&
                        !transactionDto.getType().equalsIgnoreCase("DEBIT"))) {
            logger.error("Validation failed: Invalid transaction type: {}", transactionDto.getType());
            throw new ValidationException("Transaction type must be either CREDIT or DEBIT");
        }

        if (transactionDto.getDescription() == null || transactionDto.getDescription().trim().isEmpty()) {
            logger.error("Validation failed: Description cannot be empty");
            throw new ValidationException("Description cannot be empty");
        }
    }
}