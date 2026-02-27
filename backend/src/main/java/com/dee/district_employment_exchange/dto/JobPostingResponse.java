package com.dee.district_employment_exchange.dto;

import com.dee.district_employment_exchange.entity.JobPosting;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class JobPostingResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private JobPosting.JobType jobType;
    private String qualificationsRequired;
    private String experienceRequired;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private LocalDate applicationDeadline;
    private Integer vacanciesCount;
    private JobPosting.Status status;

    // Employer info (we don't expose full User object)
    private Long employerId;
    private String employerName;
    private String employerEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
