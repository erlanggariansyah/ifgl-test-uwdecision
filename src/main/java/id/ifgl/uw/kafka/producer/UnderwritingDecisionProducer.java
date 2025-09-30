package id.ifgl.uw.kafka.producer;

import id.ifgl.uw.constants.TopicConstant;
import id.ifgl.uw.model.UnderwritingDecision;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class UnderwritingDecisionProducer {
    @Inject
    @Channel(TopicConstant.UW_OUT)
    Emitter<Record<String, UnderwritingDecision>> emitter;

    public void send(UnderwritingDecision underwritingDecision) {
        emitter.send(Record.of(underwritingDecision.applicationId, underwritingDecision));
    }
}
