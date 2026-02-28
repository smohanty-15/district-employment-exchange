package com.dee.district_employment_exchange.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    // Optional cover letter
    private String coverLetter;
}
