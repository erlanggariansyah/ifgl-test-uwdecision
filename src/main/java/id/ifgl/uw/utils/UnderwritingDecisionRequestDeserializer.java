package id.ifgl.uw.utils;

import id.ifgl.uw.dto.request.UnderwritingDecisionRequest;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class UnderwritingDecisionRequestDeserializer extends ObjectMapperDeserializer<UnderwritingDecisionRequest> {
    public UnderwritingDecisionRequestDeserializer() {
        super(UnderwritingDecisionRequest.class);
    }
}
