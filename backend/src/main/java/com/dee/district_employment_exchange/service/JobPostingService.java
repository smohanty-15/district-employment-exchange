package com.dee.district_employment_exchange.service;

import com.dee.district_employment_exchange.dto.JobPostingRequest;
import com.dee.district_employment_exchange.dto.JobPostingResponse;
import com.dee.district_employment_exchange.entity.JobPosting;
import com.dee.district_employment_exchange.entity.User;
import com.dee.district_employment_exchange.repository.JobPostingRepository;
import com.dee.district_employment_exchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;

    // CREATE JOB
    public JobPostingResponse createJob(JobPostingRequest request, String employerEmail) {

        // Get employer from DB
        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only employers can post jobs
        if(employer.getRole() !=User.Role.EMPLOYER){
            throw new RuntimeException("Only employers can post jobs");
        }

        JobPosting job = JobPosting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .qualificationsRequired(request.getQualificationsRequired())
                .experienceRequired(request.getExperienceRequired())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .applicationDeadline(request.getApplicationDeadline())
                .vacanciesCount(request.getVacanciesCount() != null ? request.getVacanciesCount() : 1)
                .employer(employer)
                .build();
        JobPosting saved = jobPostingRepository.save(job);
        return toResponse(saved);
    }

    // GET ALL ACTIVE JOBS
    public List<JobPostingResponse> getAllActiveJobs() {
        return jobPostingRepository
                .findByStatus(JobPosting.Status.ACTIVE)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // SEARCH JOBS
    public List<JobPostingResponse> searchJobs(String keyword,
                                               String location,
                                               JobPosting.JobType jobType) {
        return jobPostingRepository
                .searchJobs(keyword, location, jobType)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // GET JOB BY ID
    public JobPostingResponse getJobById(Long id) {
        JobPosting job = jobPostingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + id));
        return toResponse(job);
    }

    // GET MY JOBS (employer)
    public List<JobPostingResponse> getMyJobs(String employerEmail) {
        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jobPostingRepository
                .findByEmployerId(employer.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // UPDATE JOB
    public JobPostingResponse updateJob(Long jobId, JobPostingRequest request, String employerEmail) {

        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + jobId));

        // Check this job belongs to the logged-in employer
        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException(
                    "You can only update your own job postings");
        }

        // Update fields
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setQualificationsRequired(request.getQualificationsRequired());
        job.setExperienceRequired(request.getExperienceRequired());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setApplicationDeadline(request.getApplicationDeadline());

        if (request.getVacanciesCount() != null) {
            job.setVacanciesCount(request.getVacanciesCount());
        }

        JobPosting updated = jobPostingRepository.save(job);
        return toResponse(updated);
    }

    // DELETE JOB
    public void deleteJob(Long jobId, String employerEmail){

        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " +jobId));

        // Check this job belongs to the logged-in employer
        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException(
                    "You can only delete your own job postings");
        }

        jobPostingRepository.delete(job);
    }

    // CLOSE JOB
    public JobPostingResponse closeJob(Long jobId, String employerEmail) {

        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + jobId));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException(
                    "You can only close your own job postings");
        }

        job.setStatus(JobPosting.Status.CLOSED);
        JobPosting updated = jobPostingRepository.save(job);
        return toResponse(updated);
    }

    // CONVERT Entity → DTO
    private JobPostingResponse toResponse(JobPosting job) {
        JobPostingResponse r = new JobPostingResponse();
        r.setId(job.getId());
        r.setTitle(job.getTitle());
        r.setDescription(job.getDescription());
        r.setLocation(job.getLocation());
        r.setJobType(job.getJobType());
        r.setQualificationsRequired(job.getQualificationsRequired());
        r.setExperienceRequired(job.getExperienceRequired());
        r.setSalaryMin(job.getSalaryMin());
        r.setSalaryMax(job.getSalaryMax());
        r.setApplicationDeadline(job.getApplicationDeadline());
        r.setVacanciesCount(job.getVacanciesCount());
        r.setStatus(job.getStatus());
        r.setEmployerId(job.getEmployer().getId());
        r.setEmployerName(job.getEmployer().getName());
        r.setEmployerEmail(job.getEmployer().getEmail());
        r.setCreatedAt(job.getCreatedAt());
        r.setUpdatedAt(job.getUpdatedAt());
        return r;
    }
}
