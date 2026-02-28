package com.dee.district_employment_exchange.service;

import com.dee.district_employment_exchange.dto.ApplicationRequest;
import com.dee.district_employment_exchange.dto.ApplicationResponse;
import com.dee.district_employment_exchange.dto.ApplicationStatusRequest;
import com.dee.district_employment_exchange.entity.Application;
import com.dee.district_employment_exchange.entity.JobPosting;
import com.dee.district_employment_exchange.entity.User;
import com.dee.district_employment_exchange.repository.ApplicationRepository;
import com.dee.district_employment_exchange.repository.JobPostingRepository;
import com.dee.district_employment_exchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // APPLY FOR JOB
    public ApplicationResponse applyForJob(ApplicationRequest request,
                                           String jobSeekerEmail) {

        // Get job seeker from DB
        User jobSeeker = userRepository.findByEmail(jobSeekerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only job seekers can apply
        if (jobSeeker.getRole() != User.Role.JOB_SEEKER) {
            throw new RuntimeException(
                    "Only job seekers can apply for jobs");
        }

        // Get the job
        JobPosting job = jobPostingRepository
                .findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + request.getJobId()));

        // Check job is still active
        if (job.getStatus() != JobPosting.Status.ACTIVE) {
            throw new RuntimeException(
                    "This job is no longer accepting applications");
        }

        // Check deadline not passed
        if (job.getApplicationDeadline() != null &&
                job.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new RuntimeException(
                    "Application deadline has passed for this job");
        }

        // Check not already applied
        if (applicationRepository.existsByJobSeekerIdAndJobPostingId(
                jobSeeker.getId(), job.getId())) {
            throw new RuntimeException(
                    "You have already applied for this job");
        }

        // Create application
        Application application = Application.builder()
                .jobSeeker(jobSeeker)
                .jobPosting(job)
                .coverLetter(request.getCoverLetter())
                .build();

        Application saved = applicationRepository.save(application);

        // Send confirmation to job seeker
        emailService.sendApplicationConfirmation(
                jobSeeker.getEmail(),
                jobSeeker.getName(),
                job.getTitle(),
                job.getEmployer().getName()
        );

        // Send alert to employer
        emailService.sendNewApplicationAlert(
                job.getEmployer().getEmail(),
                job.getEmployer().getName(),
                jobSeeker.getName(),
                job.getTitle()
        );
        return toResponse(saved);
    }

    // GET MY APPLICATIONS (job seeker)
    public List<ApplicationResponse> getMyApplications(String jobSeekerEmail) {
        User jobSeeker = userRepository.findByEmail(jobSeekerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return applicationRepository
                .findByJobSeekerId(jobSeeker.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // GET APPLICANTS FOR JOB (employer)
    public List<ApplicationResponse>
    getJobApplicants(Long jobId, String employerEmail) {

        // Get the job
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        // Make sure this employer owns this job
        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("You can only view applicants for your own jobs");
        }

        return applicationRepository
                .findByJobPostingId(jobId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // GET ONE APPLICATION
    public ApplicationResponse
    getApplicationById(Long applicationId, String userEmail) {

        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new RuntimeException(
                        "Application not found with id: " + applicationId));

        // Only the job seeker or the employer can view it
        boolean isJobSeeker = application.getJobSeeker()
                .getEmail().equals(userEmail);
        boolean isEmployer = application.getJobPosting()
                .getEmployer().getEmail().equals(userEmail);
        if (!isJobSeeker && !isEmployer) {
            throw new RuntimeException(
                    "You are not authorized to view this application");
        }

        return toResponse(application);
    }

    // UPDATE APPLICATION STATUS (employer only)
    public ApplicationResponse updateStatus(Long applicationId,
                                            ApplicationStatusRequest request,
                                            String employerEmail) {

        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new RuntimeException(
                        "Application not found with id: " + applicationId));

        // Make sure this employer owns the job this application is for
        if (!application.getJobPosting().getEmployer()
                .getEmail().equals(employerEmail)) {
            throw new RuntimeException("You can only update applications for your own jobs");
        }

        application.setStatus(request.getStatus());

        if (request.getEmployerNotes() != null) {
            application.setEmployerNotes(request.getEmployerNotes());
        }

        Application updated = applicationRepository.save(application);

        // Send status update email to job seeker
        emailService.sendStatusUpdateEmail(
                application.getJobSeeker().getEmail(),
                application.getJobSeeker().getName(),
                application.getJobPosting().getTitle(),
                request.getStatus().name(),
                request.getEmployerNotes()
        );

        return toResponse(updated);
    }

    // CONVERT Entity → DTO
    private ApplicationResponse toResponse(Application application) {
        ApplicationResponse r = new ApplicationResponse();

        // Job seeker info
        r.setJobSeekerId(application.getJobSeeker().getId());
        r.setJobSeekerName(application.getJobSeeker().getName());
        r.setJobSeekerEmail(application.getJobSeeker().getEmail());

        // Job info
        r.setJobId(application.getJobPosting().getId());
        r.setJobTitle(application.getJobPosting().getTitle());
        r.setJobLocation(application.getJobPosting().getLocation());
        r.setEmployerName(application.getJobPosting()
                .getEmployer().getName());

        // Application info
        r.setCoverLetter(application.getCoverLetter());
        r.setStatus(application.getStatus());
        r.setEmployerNotes(application.getEmployerNotes());
        r.setAppliedAt(application.getAppliedAt());
        r.setUpdatedAt(application.getUpdatedAt());

        return r;
    }
}