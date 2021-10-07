package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.PaymentRepository;
import com.endava.internship.mocking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private PaymentService paymentService;

    private UserRepository userRepository;
    private PaymentRepository paymentRepository;
    private ValidationService validationService;
    private User sampleUser1;
    private User sampleUser2;
    private User sampleUser3;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        paymentRepository = mock(PaymentRepository.class);
        validationService = mock(ValidationService.class);

        paymentService = new PaymentService(userRepository, paymentRepository, validationService);

        sampleUser1 = new User(1, "Rita", Status.ACTIVE);
        sampleUser2 = new User(2, "Dito", Status.ACTIVE);
        sampleUser3 = new User(3, "Vasya", Status.INACTIVE);
    }

    @Test
    void createPayment() {
        when(userRepository.findById(2)).thenReturn(Optional.of(sampleUser2));
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        paymentService.createPayment(2, 500.0);

        verify(validationService).validateUserId(2);
        verify(validationService).validateAmount(500.0);
        verify(validationService).validateUser(sampleUser2);
        verify(paymentRepository).save(paymentCaptor.capture());

        assertThat(paymentCaptor.getValue().getPaymentId()).isNotNull();
        assertThat(paymentCaptor.getValue().getUserId()).isEqualTo(sampleUser2.getId());
        assertThat(paymentCaptor.getValue().getMessage()).contains(sampleUser2.getName());
        assertThat(paymentCaptor.getValue().getMessage()).isNotBlank();
        assertThat(paymentCaptor.getValue().getAmount()).isEqualTo(500.);
    }

    @Test
    void editMessage() {
        Payment pay = new Payment(1,65.0,"Successful.");
        UUID i = pay.getPaymentId();
        paymentService.editPaymentMessage(i, "Transaction finished.");

        verify(validationService).validatePaymentId(notNull());
        verify(validationService).validateMessage("Transaction finished.");
        verify(paymentRepository).editMessage(i,"Transaction finished.");
    }

    @Test
    void getAllByAmountExceeding() {
        paymentService.getAllByAmountExceeding(500.);

        assertAll(
                () ->   verify(paymentRepository).findAll()
        );
    }
}
