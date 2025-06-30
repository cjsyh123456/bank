package com.example.bank.controller;

import com.example.bank.dto.TransactionDto;
import com.example.bank.dto.TransactionResponse;
import com.example.bank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction API", description = "Operations pertaining to transactions in Banking System")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
        logger.info("TransactionController initialized with TransactionService: {}",
                transactionService.getClass().getSimpleName());
    }

    @Operation(summary = "Create a new transaction", description = "Creates a new banking transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Parameter(description = "Transaction object to be created", required = true)
            @RequestBody TransactionDto transactionDto) {
        logger.debug("Received request to create transaction: {}", transactionDto);
        TransactionResponse response = transactionService.createTransaction(transactionDto);
        logger.info("Transaction created successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing transaction", description = "Updates details of an existing transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @Parameter(description = "ID of the transaction to be updated", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated transaction object", required = true)
            @RequestBody TransactionDto transactionDto) {
        logger.debug("Received request to update transaction ID: {} with data: {}", id, transactionDto);
        TransactionResponse response = transactionService.updateTransaction(id, transactionDto);
        logger.info("Transaction updated successfully with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a transaction", description = "Deletes a transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "ID of the transaction to be deleted", required = true)
            @PathVariable UUID id) {
        logger.debug("Received request to delete transaction with ID: {}", id);
        transactionService.deleteTransaction(id);
        logger.info("Transaction deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a transaction by ID", description = "Returns a single transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @Parameter(description = "ID of the transaction to be retrieved", required = true)
            @PathVariable UUID id) {
        logger.debug("Received request to get transaction with ID: {}", id);
        TransactionResponse response = transactionService.getTransactionById(id);
        logger.debug("Returning transaction with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all transactions", description = "Returns a list of all transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        logger.debug("Received request to get all transactions");
        List<TransactionResponse> responses = transactionService.getAllTransactions();
        logger.info("Returning {} transactions", responses.size());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get paginated transactions", description = "Returns a paginated list of transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved page",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/paged")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactionsPaged(Pageable pageable) {
        logger.debug("Received request to get paginated transactions. Page: {}, Size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        Page<TransactionResponse> responses = transactionService.getAllTransactions(pageable);
        logger.debug("Returning page {} of {} with {} items",
                pageable.getPageNumber(), responses.getTotalPages(), responses.getNumberOfElements());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get transactions by type", description = "Returns transactions filtered by type (CREDIT/DEBIT)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered list",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid type parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByType(
            @Parameter(description = "Type of transactions to filter by (CREDIT/DEBIT)", required = true)
            @PathVariable String type) {
        logger.debug("Received request to get transactions by type: {}", type);
        List<TransactionResponse> responses = transactionService.getTransactionsByType(type);
        logger.info("Returning {} transactions of type: {}", responses.size(), type);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get transactions by category", description = "Returns transactions filtered by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered list",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByCategory(
            @Parameter(description = "Category of transactions to filter by", required = true)
            @PathVariable String category) {
        logger.debug("Received request to get transactions by category: {}", category);
        List<TransactionResponse> responses = transactionService.getTransactionsByCategory(category);
        logger.info("Returning {} transactions in category: {}", responses.size(), category);
        return ResponseEntity.ok(responses);
    }
}