package com.dee.district_employment_exchange.controller;

import com.dee.district_employment_exchange.dto.ApiResponse;
import com.dee.district_employment_exchange.dto.ApplicationRequest;
import com.dee.district_employment_exchange.dto.ApplicationResponse;
import com.dee.district_employment_exchange.dto.ApplicationStatusRequest;
import com.dee.district_employment_exchange.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // Helper - get logged in user email
    private String getCurrentUserEmail(){
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        return auth.getName();
    }

    // JOB SEEKER - apply for a job
    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationResponse>>
            applyForJob(@Valid @RequestBody ApplicationRequest request) {
        String email = getCurrentUserEmail();
        ApplicationResponse application = applicationService
                .applyForJob(request, email);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Application submitted successfully", application));
    }

    // JOB SEEKER - see my applications
    @GetMapping("/my-applications")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>>
            getMyApplications(){

        String email = getCurrentUserEmail();
        List<ApplicationResponse> applications =
                applicationService.getMyApplications(email);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Applications retrieved", applications));
    }

    // EMPLOYER - see all applicants for a job
    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>>
            getJobApplicants(@PathVariable Long jobId) {

        String email = getCurrentUserEmail();
        List<ApplicationResponse> applications =
                applicationService.getJobApplicants(jobId, email);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Applicants retrieved", applications));
    }

    // BOTH - view one application
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponse>>
                 getApplicationById(@PathVariable Long id) {

        String email = getCurrentUserEmail();
        ApplicationResponse application =
                applicationService.getApplicationById(id, email);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Application retrieved", application));
    }

    // EMPLOYER - update application status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ApplicationResponse>>
    updateStatus(@PathVariable Long id,
                 @Valid @RequestBody ApplicationStatusRequest request) {

        String email = getCurrentUserEmail();
        ApplicationResponse application =
                applicationService.updateStatus(id, request, email);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Application status updated", application));
    }
}