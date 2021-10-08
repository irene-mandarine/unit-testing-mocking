package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BasicValidationServiceTest {

    private BasicValidationService basicValidationService;

    @BeforeEach
    void setUp() {
        basicValidationService = new BasicValidationService();
    }

    @Test
    void validateAmountTestZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            basicValidationService.validateAmount(0.);
        }, "Amount must be greater than 0");
    }

    @Test
    void validateAmountTestNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            basicValidationService.validateAmount(null);
        }, "Amount must not be null");
    }

    @Test
    void validateAmountTest() {
        assertDoesNotThrow(()->basicValidationService.validateAmount(150.));
    }

    @Test
    void validatePaymentIdTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            basicValidationService.validatePaymentId(null);
        }, "Payment id must not be null");
    }

    @Test
    void validateUserIdTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            basicValidationService.validateUserId(null);
        }, "User id must not be null");
    }

    @Test
    void validateUserTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            basicValidationService.validateUser(new User(1, "Nick", Status.INACTIVE));
        }, "User with id 1 not in ACTIVE status");
    }

    @Test
    void validateMessageTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            basicValidationService.validateMessage(null);
        }, "Payment message must not be null");
    }

}