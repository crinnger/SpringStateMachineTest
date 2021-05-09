package br.com.crinnger.services;

import org.springframework.statemachine.StateMachine;

import br.com.crinnger.domain.Payment;
import br.com.crinnger.domain.PaymentEvent;
import br.com.crinnger.domain.PaymentState;

public interface PaymentService {
	
	Payment newPayment(Payment payment);
	
	StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);
	
	StateMachine<PaymentState, PaymentEvent> authorize(Long paymentId);
	
	StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId); 
}
