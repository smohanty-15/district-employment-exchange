package com.dee.district_employment_exchange.dto;

import lombok.Data;

@Data
public class AdminDashboardResponse {

    private long totalUsers;
    private long totalJobSeekers;
    private long totalEmployers;
    private long totalAdmins;

    private long totalJobs;
    private long activeJobs;
    private long closedJobs;

    private long totalApplications;
    private long submittedApplications;
    private long selectedApplications;
    private long rejectedApplications;
}