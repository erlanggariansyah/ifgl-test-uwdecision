package id.ifgl.uw.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnderwritingDecisionRequest {
    public String applicationId;
    public Applicant applicant;
    public Policy policy;
    public Medical medical;
    public Financial financial;

    public static class Applicant { public String name; public Integer age; public String gender; public Boolean smoker; }
    public static class Policy { public String productCode; public Double sumInsured; public Double premium; }
    public static class Medical { public Integer bpSystolic; public Integer bpDiastolic; public Integer cholesterol; }
    public static class Financial { public Double income; public String employmentType; }
}
