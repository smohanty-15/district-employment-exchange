package com.dee.district_employment_exchange.dto;

import com.dee.district_employment_exchange.entity.Application;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationResponse {

    private Long id;

    // Job seeker info
    private Long jobSeekerId;
    private String jobSeekerName;
    private String jobSeekerEmail;

    // Job info
    private Long jobId;
    private String jobTitle;
    private String jobLocation;
    private String employerName;

    // Application info
    private String coverLetter;
    private Application.Status status;
    private String employerNotes;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
