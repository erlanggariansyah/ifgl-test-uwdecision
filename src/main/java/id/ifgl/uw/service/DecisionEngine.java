package id.ifgl.uw.service;

import id.ifgl.uw.constants.MessageConstant;
import id.ifgl.uw.enumeration.DecisionEnum;
import id.ifgl.uw.dto.request.UnderwritingDecisionRequest;
import id.ifgl.uw.kafka.producer.UnderwritingDecisionProducer;
import id.ifgl.uw.model.UnderwritingDecision;
import id.ifgl.uw.repository.UnderwritingDecisionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;

@ApplicationScoped
public class DecisionEngine {
    @Inject
    UnderwritingDecisionProducer underwritingDecisionProducer;

    @Inject
    UnderwritingDecisionRepository underwritingDecisionRepository;

    public UnderwritingDecision evaluate(UnderwritingDecisionRequest underwritingDecisionRequest) {
        UnderwritingDecision underwritingDecision = new UnderwritingDecision();
        underwritingDecision.applicationId = underwritingDecisionRequest.applicationId;

        underwritingDecision.riskScore = 100.0;

        if (underwritingDecision.riskScore <= 30) {
            underwritingDecision.decision = DecisionEnum.AUTO_APPROVE.name();
            underwritingDecision.decisionReason = MessageConstant.RESIKO_RENDAH;
        } else if (underwritingDecision.riskScore <= 60) {
            underwritingDecision.decision = DecisionEnum.MANUAL_REVIEW.name();
            underwritingDecision.decisionReason = MessageConstant.RESIKO_MENENGAH;
        } else {
            underwritingDecision.decision = DecisionEnum.DECLINE.name();
            underwritingDecision.decisionReason = MessageConstant.RESIKO_TINGGI;
        }

        underwritingDecision.processedAt = Instant.now();
        underwritingDecision.rawPayload = underwritingDecisionRequest;

        underwritingDecisionProducer.send(underwritingDecision);
        underwritingDecisionRepository.persist(underwritingDecision);

        return underwritingDecision;
    }
}
