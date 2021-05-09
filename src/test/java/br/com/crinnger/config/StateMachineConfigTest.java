package br.com.crinnger.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import br.com.crinnger.domain.PaymentEvent;
import br.com.crinnger.domain.PaymentState;

@SpringBootTest
class StateMachineConfigTest {
	
	@Autowired
	StateMachineFactory<PaymentState, PaymentEvent> factory;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	void testNewStateMachine() {
		StateMachine<PaymentState, PaymentEvent> sm=factory.getStateMachine(UUID.randomUUID());
		sm.start();
		System.out.println(sm.getState().toString());
		sm.sendEvent(PaymentEvent.PRE_AUTHORIZE);
		System.out.println(sm.getState().toString());
		sm.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
		System.out.println(sm.getState().toString());
		sm.sendEvent(PaymentEvent.PRE_AUTH_DECLINED);
		System.out.println(sm.getState().toString());
	}

}
