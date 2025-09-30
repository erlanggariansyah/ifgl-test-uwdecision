package id.ifgl.uw.resources;

import id.ifgl.uw.constants.PathConstant;
import id.ifgl.uw.dto.request.UnderwritingDecisionRequest;
import id.ifgl.uw.service.DecisionEngine;
import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(PathConstant.V1_UW_RESOURCE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UWResource {
    @Inject
    DecisionEngine decisionEngine;

    @POST
    public Response post(@NotNull UnderwritingDecisionRequest underwritingDecisionRequest) {
        decisionEngine.evaluate(underwritingDecisionRequest);
        return Response.ok().build();
    }
}
