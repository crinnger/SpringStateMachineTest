package br.com.crinnger.config.guards;

import br.com.crinnger.domain.PaymentEvent;
import br.com.crinnger.domain.PaymentState;
import br.com.crinnger.services.PaymentServiceImpl;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class PaymentIdGuard implements Guard<PaymentState, PaymentEvent> {


    @Override
    public boolean evaluate(StateContext<PaymentState, PaymentEvent> context) {
        return context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)!=null;
    }
}
