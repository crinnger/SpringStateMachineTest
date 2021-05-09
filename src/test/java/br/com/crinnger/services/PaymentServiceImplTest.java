package br.com.crinnger.services;

import static org.junit.jupiter.api.Assertions.*;

import br.com.crinnger.domain.Payment;
import br.com.crinnger.domain.PaymentEvent;
import br.com.crinnger.domain.PaymentState;
import br.com.crinnger.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@SpringBootTest
class PaymentServiceImplTest {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private PaymentService paymentService;

	private Payment payment;

	@BeforeEach
	void setUpBeforeClass() throws Exception {
		payment = Payment.builder().amount(BigDecimal.valueOf(1000)).build();
	}

	@Transactional
	@Test
	void preAuth() {
		Payment savedPayment = paymentService.newPayment(payment);

		System.out.println("Should be new");
		System.out.println(savedPayment.getState());

		StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());

		Payment preAuthedPayment = paymentRepository.getOne(savedPayment.getId());

		System.out.println("Should be PRE_AUTH or PRE_AUTH_ERROR");
		System.out.println(sm.getState().getId());

		System.out.println(preAuthedPayment);

	}

	@Transactional
	@RepeatedTest(10)
	void Auth() {
		Payment savedPayment = paymentService.newPayment(payment);

		System.out.println("Should be new");
		System.out.println(savedPayment.getState());

		StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());

		Payment preAuthedPayment = paymentRepository.getOne(savedPayment.getId());

		System.out.println("Should be PRE_AUTH or PRE_AUTH_ERROR");
		System.out.println(sm.getState().getId());
		System.out.println(preAuthedPayment);

		if(sm.getState().getId().equals(PaymentState.PRE_AUTH)){
			StateMachine<PaymentState, PaymentEvent> smAuth = paymentService.authorize(savedPayment.getId());
			Payment authedPayment = paymentRepository.getOne(savedPayment.getId());
			System.out.println("Should be AUTH or AUTH_ERROR");
			System.out.println(smAuth.getState().getId());
			System.out.println(authedPayment);
		}
	}
}
