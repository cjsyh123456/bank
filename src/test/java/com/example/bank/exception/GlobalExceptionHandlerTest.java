package com.example.bank.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private final WebRequest mockRequest = mock(WebRequest.class);

    @Test
    void handleTransactionNotFoundException_ShouldReturnNotFoundResponse() {
        // Arrange
        String errorMessage = "Transaction not found with id: 123";
        TransactionNotFoundException ex = new TransactionNotFoundException(errorMessage);

        // Act
        ResponseEntity<Object> response =
                globalExceptionHandler.handleTransactionNotFoundException(ex, mockRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.get("message"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleValidationException_ShouldReturnBadRequestResponse() {
        // Arrange
        String errorMessage = "Amount must be greater than zero";
        ValidationException ex = new ValidationException(errorMessage);

        // Act
        ResponseEntity<Object> response =
                globalExceptionHandler.handleValidationException(ex, mockRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.get("message"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerErrorResponse() {
        // Arrange
        Exception ex = new Exception("Some unexpected error");

        // Act
        ResponseEntity<Object> response =
                globalExceptionHandler.handleGlobalException(ex, mockRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("An error occurred while processing your request", body.get("message"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleGlobalException_ShouldUseStandardMessage_RegardlessOfExceptionMessage() {
        // Arrange
        String customMessage = "Custom error details";
        Exception ex = new Exception(customMessage);

        // Act
        ResponseEntity<Object> response =
                globalExceptionHandler.handleGlobalException(ex, mockRequest);

        // Assert
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("An error occurred while processing your request", body.get("message"));
    }

    @Test
    void responseBody_ShouldContainOnlyTimestampAndMessage() {
        // Arrange
        String errorMessage = "Test error message";
        TransactionNotFoundException ex = new TransactionNotFoundException(errorMessage);

        // Act
        ResponseEntity<Object> response =
                globalExceptionHandler.handleTransactionNotFoundException(ex, mockRequest);

        // Assert
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(2, body.size());
        assertTrue(body.containsKey("timestamp"));
        assertTrue(body.containsKey("message"));
    }

    @Test
    void timestampInResponse_ShouldBeCurrentTime() {
        // Arrange
        LocalDateTime beforeTest = LocalDateTime.now();
        TransactionNotFoundException ex = new TransactionNotFoundException("Test");

        // Act
        ResponseEntity<Object> response =
                globalExceptionHandler.handleTransactionNotFoundException(ex, mockRequest);

        LocalDateTime afterTest = LocalDateTime.now();

        // Assert
        @SuppressWarnings("unchecked")
        LocalDateTime responseTime = (LocalDateTime) ((Map<String, Object>) response.getBody()).get("timestamp");

        assertFalse(responseTime.isBefore(beforeTest), "Timestamp should not be before test start");
        assertFalse(responseTime.isAfter(afterTest), "Timestamp should not be after test end");
    }
}