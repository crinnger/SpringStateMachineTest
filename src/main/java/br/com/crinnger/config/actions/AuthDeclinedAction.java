package br.com.crinnger.config.actions;

import br.com.crinnger.domain.PaymentEvent;
import br.com.crinnger.domain.PaymentState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class AuthDeclinedAction implements Action<PaymentState, PaymentEvent> {

    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        System.out.println("Payment Auth Declined");
    }
}
