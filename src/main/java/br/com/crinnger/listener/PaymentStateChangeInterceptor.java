package br.com.crinnger.listener;

import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import br.com.crinnger.domain.Payment;
import br.com.crinnger.domain.PaymentEvent;
import br.com.crinnger.domain.PaymentState;
import br.com.crinnger.repository.PaymentRepository;
import br.com.crinnger.services.PaymentService;
import br.com.crinnger.services.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent>{
	private final PaymentRepository repository;

	@Override
	public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message,
			Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine) {
		// TODO Auto-generated method stub
		Optional.ofNullable(message).ifPresent(msg -> {
			Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER,-1L)))
				    .ifPresent( paymentId -> {
				    	Payment payment = repository.getOne(paymentId);
				    	payment.setState(state.getId());
				    	repository.save(payment); 
				    });
		});
	}
	

}
