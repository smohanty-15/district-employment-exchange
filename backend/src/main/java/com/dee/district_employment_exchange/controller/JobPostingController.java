package com.dee.district_employment_exchange.controller;

import com.dee.district_employment_exchange.dto.ApiResponse;
import com.dee.district_employment_exchange.dto.JobPostingRequest;
import com.dee.district_employment_exchange.dto.JobPostingResponse;
import com.dee.district_employment_exchange.entity.JobPosting;
import com.dee.district_employment_exchange.service.JobPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    // Helper - get logged in user's email from JWT
    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    // PUBLIC - anyone can view all active jobs
    @GetMapping
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>>
            getAllJobs() {
        List<JobPostingResponse> jobs =
                jobPostingService.getAllActiveJobs();
        return ResponseEntity.ok(
                ApiResponse.success("Jobs retrieved", jobs));
    }

    // PUBLIC - search jobs with filters
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>>
            searchJobs(
                    @RequestParam(required = false) String keyword,
                    @RequestParam(required = false) String location,
                    @RequestParam(required = false) JobPosting.JobType jobType) {

        List<JobPostingResponse> jobs =
                jobPostingService.searchJobs(keyword, location, jobType);

        return ResponseEntity.ok(
                ApiResponse.success(jobs.size() + " job(s) found", jobs));
    }

    // PUBLIC - anyone can view one job
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostingResponse>>
            getJobById(@PathVariable Long id) {
        JobPostingResponse job = jobPostingService.getJobById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Job retrieved", job));
    }

    // EMPLOYER ONLY - create job
    @PostMapping
    public ResponseEntity<ApiResponse<JobPostingResponse>>
            createJob(@Valid @RequestBody JobPostingRequest request) {
        String email = getCurrentUserEmail();
        JobPostingResponse job =
                jobPostingService.createJob(request, email);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job created successfully", job));
    }

    // EMPLOYER ONLY - update own job
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostingResponse>>
            updateJob(@PathVariable Long id, @Valid @RequestBody JobPostingRequest request) {
        String email = getCurrentUserEmail();
        JobPostingResponse job =
                jobPostingService.updateJob(id, request, email);
        return ResponseEntity.ok(
                ApiResponse.success("Job updated successfully", job));
    }

    // EMPLOYER ONLY - delete own job
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>>
            deleteJob(@PathVariable Long id){
        String email =getCurrentUserEmail();
        jobPostingService.deleteJob(id,email);
        return ResponseEntity.ok(
                ApiResponse.success("Job deleted successfully"));
    }

    // EMPLOYER ONLY - see only my job postings
    @GetMapping("/my-postings")
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>>
            getMyJobs(){
        String email = getCurrentUserEmail();
        List<JobPostingResponse> jobs =
                jobPostingService.getMyJobs(email);
        return ResponseEntity.ok(
                ApiResponse.success("Your job postings retrieved", jobs));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<ApiResponse<JobPostingResponse>>
    closeJob(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        JobPostingResponse job = jobPostingService.closeJob(id, email);
        return ResponseEntity.ok(
                ApiResponse.success("Job closed successfully", job));
    }
}