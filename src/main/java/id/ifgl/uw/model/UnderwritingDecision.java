package id.ifgl.uw.model;

import id.ifgl.uw.constants.ModelConstant;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.Instant;

@MongoEntity(collection = ModelConstant.UNDERWRITING_DECISIONS)
public class UnderwritingDecision extends PanacheMongoEntity {
    public String applicationId;
    public double riskScore;
    public String decision; // AUTOMATIC_APPROVE | MANUAL_REVIEW | DECLINE
    public String decisionReason;
    public Instant processedAt;
    public Object rawPayload;
}
