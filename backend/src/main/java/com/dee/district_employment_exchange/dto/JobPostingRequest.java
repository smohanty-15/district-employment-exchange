package com.dee.district_employment_exchange.dto;

import com.dee.district_employment_exchange.entity.JobPosting;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class JobPostingRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Job type is required")
    private JobPosting.JobType jobType;

    private String qualificationsRequired;

    private String experienceRequired;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private LocalDate applicationDeadline;

    private Integer vacanciesCount;
}
