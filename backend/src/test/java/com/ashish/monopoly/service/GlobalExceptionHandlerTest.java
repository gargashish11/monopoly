package com.ashish.monopoly.service;

import com.ashish.monopoly.data.ErrorResponse;
import com.ashish.monopoly.service.exception.GlobalExceptionHandler;
import com.ashish.monopoly.service.exception.InsufficientBalanceException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void testHandleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Item not found");
        ResponseEntity<ErrorResponse> response = handler.handleEntityNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Item not found", response.getBody().getMessage());
    }

    @Test
    public void testHandleBusinessExceptions() {
        InsufficientBalanceException ex = new InsufficientBalanceException("Not enough balance");
        ResponseEntity<ErrorResponse> response = handler.handleBusinessExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Not enough balance", response.getBody().getMessage());
    }

    @Test
    public void testHandleGlobalException() {
        RuntimeException ex = new RuntimeException("Unexpected error");
        ResponseEntity<ErrorResponse> response = handler.handleGlobalException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("An unexpected error occurred. Please try again.", response.getBody().getMessage());
    }

    @Test
    public void testErrorResponseGettersAndSetters() {
        LocalDateTime time = LocalDateTime.now();
        ErrorResponse resp = new ErrorResponse(400, "Bad Request", "Error msg", time);

        assertEquals(400, resp.getStatus());
        assertEquals("Bad Request", resp.getError());
        assertEquals("Error msg", resp.getMessage());
        assertEquals(time, resp.getTimestamp());

        resp.setStatus(404);
        resp.setError("Not Found");
        resp.setMessage("New message");
        LocalDateTime newTime = time.plusMinutes(1);
        resp.setTimestamp(newTime);

        assertEquals(404, resp.getStatus());
        assertEquals("Not Found", resp.getError());
        assertEquals("New message", resp.getMessage());
        assertEquals(newTime, resp.getTimestamp());
    }
}
