package id.ifgl.uw.service;

import id.ifgl.uw.constants.DecisionEnum;
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

        int age = underwritingDecisionRequest.applicant != null && underwritingDecisionRequest.applicant.age != null ? underwritingDecisionRequest.applicant.age : 40;
        double sumInsured = underwritingDecisionRequest.policy != null && underwritingDecisionRequest.policy.sumInsured != null ? underwritingDecisionRequest.policy.sumInsured : 0.0;
        double income = underwritingDecisionRequest.financial != null && underwritingDecisionRequest.financial.income != null ? underwritingDecisionRequest.financial.income : 1.0;

        double ageScore = Math.max(0, Math.min(100, (age - 18) * 2));

        double med = 0;
        if (underwritingDecisionRequest.medical != null) {
            med += underwritingDecisionRequest.medical.bpSystolic != null && underwritingDecisionRequest.medical.bpSystolic > 140 ? 40 : 0;
            med += underwritingDecisionRequest.medical.cholesterol != null && underwritingDecisionRequest.medical.cholesterol > 240 ? 30 : 0;
            med += (underwritingDecisionRequest.applicant != null && Boolean.TRUE.equals(underwritingDecisionRequest.applicant.smoker)) ? 30 : 0;
        }

        double medicalScore = Math.min(100, med);

        double ratio = income > 0 ? sumInsured / income : sumInsured;
        double financialScore = Math.min(100, Math.log10(ratio + 1) * 10);

        double productScore = sumInsured > 500_000_000 ? 30 : (sumInsured > 100_000_000 ? 10 : 0);

        double riskScore = ageScore * 0.3 + medicalScore * 0.4 + financialScore * 0.2 + productScore * 0.1;
        underwritingDecision.riskScore = Math.round(riskScore * 100.0) / 100.0;

        if (underwritingDecision.riskScore <= 30) {
            underwritingDecision.decision = DecisionEnum.AUTO_APPROVE.name();
            underwritingDecision.decisionReason = "Resiko rendah";
        } else if (underwritingDecision.riskScore <= 60) {
            underwritingDecision.decision = DecisionEnum.MANUAL_REVIEW.name();
            underwritingDecision.decisionReason = "Resiko menengah";
        } else {
            underwritingDecision.decision = DecisionEnum.DECLINE.name();
            underwritingDecision.decisionReason = "Resiko tinggi";
        }

        underwritingDecision.processedAt = Instant.now();
        underwritingDecision.rawPayload = underwritingDecisionRequest;

        underwritingDecisionProducer.send(underwritingDecision);
        underwritingDecisionRepository.persist(underwritingDecision);

        return underwritingDecision;
    }
}
