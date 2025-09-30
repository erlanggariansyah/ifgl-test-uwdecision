package id.ifgl.uw.kafka.consumer;

import id.ifgl.uw.dto.UnderwritingDecisionRequest;
import id.ifgl.uw.service.DecisionEngine;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class UnderwritingDecisionConsumer {
    @Inject
    DecisionEngine decisionEngine;

    @Incoming("underwriting-input")
    public void receive(Record<Integer, UnderwritingDecisionRequest> record) {
        decisionEngine.evaluate(record.value());
    }
}
