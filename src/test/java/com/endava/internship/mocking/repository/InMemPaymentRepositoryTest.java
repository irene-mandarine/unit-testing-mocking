package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemPaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Payment p1;
    private Payment p2;
    private Payment p3;


    @BeforeEach
    void setUp() {
        paymentRepository = new InMemPaymentRepository();
        p1 = new Payment(1, 500., "Success.");
        p2 = new Payment(1, 500., "Success.");
        p3 = new Payment(1, 500., "Success.");
    }

    @Test
    void findByIdTestNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentRepository.findById(null);
        }, "Payment id must not be null");
    }

    @Test
    void findByIdTestNotNull() {
        UUID id = p1.getPaymentId();
        paymentRepository.save(p1);

        assertThat(paymentRepository.findById(id).isPresent()).isTrue();
        assertThat(paymentRepository.findById(id).get()).isEqualTo(p1);
    }

    @Test
    void findAllTest() {
        List<Payment> list = Arrays.asList(p1, p2, p3);

        paymentRepository.save(p1);
        paymentRepository.save(p2);
        paymentRepository.save(p3);

        assertThat(paymentRepository.findAll()).containsAll(list);
    }

    @Test
    void saveTestNullPayment() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentRepository.save(null);
        }, "Payment must not be null");
    }

    @Test
    void saveTestNonExistentId() {
        paymentRepository.save(p1);
        assertThrows(IllegalArgumentException.class, () -> {
            paymentRepository.save(p1);
        }, ("Payment with id " + p1.getPaymentId() + "already saved"));
    }

    @Test
    void saveTestWorksCorrectly() {
        assertThat(paymentRepository.save(p1)).isEqualTo(p1);
    }

    @Test
    void editMessageTestNullPayment() {
        assertThrows(NoSuchElementException.class, () -> {
            paymentRepository.editMessage(p1.getPaymentId(), "Message");
        });
    }

    @Test
    void editMessageTestPayment() {
        paymentRepository.save(p1);
        assertThat(paymentRepository.editMessage(p1.getPaymentId(), "Message")).
                    isEqualTo(p1);
        assertThat(paymentRepository.editMessage(p1.getPaymentId(), "Message").
                    getMessage()).isEqualTo("Message");
    }
}