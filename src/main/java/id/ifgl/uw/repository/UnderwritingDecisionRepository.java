package id.ifgl.uw.repository;

import id.ifgl.uw.model.UnderwritingDecision;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UnderwritingDecisionRepository implements PanacheMongoRepository<UnderwritingDecision> {
}
