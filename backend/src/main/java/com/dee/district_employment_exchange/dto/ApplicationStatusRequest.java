package com.dee.district_employment_exchange.dto;

import com.dee.district_employment_exchange.entity.Application;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationStatusRequest {

    @NotNull(message = "Status is required")
    private Application.Status status;

    // Optional notes from employer
    private String employerNotes;
}
