package br.com.crinnger.services;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import br.com.crinnger.domain.Payment;
import br.com.crinnger.domain.PaymentEvent;
import br.com.crinnger.domain.PaymentState;
import br.com.crinnger.listener.PaymentStateChangeInterceptor;
import br.com.crinnger.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService{
	
	public static final String PAYMENT_ID_HEADER= "payment_id";
	private final PaymentRepository repository;
	private final StateMachineFactory<PaymentState, PaymentEvent> stateMachine;
	private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;

	@Override
	public Payment newPayment(Payment payment) { 
		// TODO Auto-generated method stub
		payment.setState(PaymentState.NEW);
		return repository.save(payment);
	}

	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
		// TODO Auto-generated method stub
		StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
		senEvent(paymentId, sm, PaymentEvent.PRE_AUTHORIZE);
		return sm;
	}

	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> authorize(Long paymentId) {
		// TODO Auto-generated method stub
		StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);		
		senEvent(paymentId, sm, PaymentEvent.AUTHORIZE);
		return sm;
	}


	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
		// TODO Auto-generated method stub
		StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
		senEvent(paymentId, sm, PaymentEvent.AUTH_DECLINED);
		return sm;
	}
	
	private StateMachine<PaymentState, PaymentEvent> build(Long id){
		Payment pay = repository.getOne(id);
		
		StateMachine<PaymentState, PaymentEvent> sm = stateMachine.getStateMachine(Long.toString(pay.getId()));
		sm.stop();
		
		sm.getStateMachineAccessor().doWithAllRegions(sma->{
			sma.addStateMachineInterceptor(paymentStateChangeInterceptor); 
			sma.resetStateMachine(new DefaultStateMachineContext<PaymentState, PaymentEvent>(pay.getState(),null, null, null));
		});
		sm.start();
		return sm;
	}
	
	private void senEvent(Long id, StateMachine<PaymentState, PaymentEvent> sm, PaymentEvent event) {
		Message msg = MessageBuilder.withPayload(event)
								    .setHeader(PAYMENT_ID_HEADER, id)
								    .build();
		sm.sendEvent(msg);
	}

}
