package com.dee.district_employment_exchange.service;

import com.dee.district_employment_exchange.dto.AdminDashboardResponse;
import com.dee.district_employment_exchange.dto.JobPostingResponse;
import com.dee.district_employment_exchange.dto.UserResponse;
import com.dee.district_employment_exchange.entity.Application;
import com.dee.district_employment_exchange.entity.JobPosting;
import com.dee.district_employment_exchange.entity.User;
import com.dee.district_employment_exchange.repository.ApplicationRepository;
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
public class AdminService {

    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;

    // DASHBOARD STATS
    public AdminDashboardResponse getDashboard() {
        AdminDashboardResponse dashboard = new AdminDashboardResponse();

        // User stats
        dashboard.setTotalUsers(userRepository.count());
        dashboard.setTotalJobSeekers(
                userRepository.countByRole(User.Role.JOB_SEEKER));
        dashboard.setTotalEmployers(
                userRepository.countByRole(User.Role.EMPLOYER));
        dashboard.setTotalAdmins(
                userRepository.countByRole(User.Role.ADMIN));

        // Job stats
        dashboard.setTotalJobs(jobPostingRepository.count());
        dashboard.setActiveJobs(
                jobPostingRepository.countByStatus(JobPosting.Status.ACTIVE));
        dashboard.setClosedJobs(
                jobPostingRepository.countByStatus(JobPosting.Status.CLOSED));

        // Application stats
        dashboard.setTotalApplications(applicationRepository.count());
        dashboard.setSubmittedApplications(
                applicationRepository.countByStatus(Application.Status.SUBMITTED));
        dashboard.setSelectedApplications(
                applicationRepository.countByStatus(Application.Status.SELECTED));
        dashboard.setRejectedApplications(
                applicationRepository.countByStatus(Application.Status.REJECTED));

        return dashboard;
    }

    // GET ALL USERS
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    // TOGGLE USER STATUS
    public UserResponse toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id: " + userId));

        // Flip active status
        user.setIsActive(!user.getIsActive());
        User updated = userRepository.save(user);
        return toUserResponse(updated);
    }

    // DELETE USER
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id: " + userId));
        userRepository.delete(user);
    }

    // GET ALL JOBS
    public List<JobPostingResponse> getAllJobs() {
        return jobPostingRepository.findAll()
                .stream()
                .map(this::toJobResponse)
                .collect(Collectors.toList());
    }

    // SUSPEND JOB
    public JobPostingResponse suspendJob(Long jobId) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + jobId));

        job.setStatus(JobPosting.Status.SUSPENDED);
        JobPosting updated = jobPostingRepository.save(job);
        return toJobResponse(updated);
    }

    // CONVERT User → DTO
    private UserResponse toUserResponse(User user) {
        UserResponse r = new UserResponse();
        r.setId(user.getId());
        r.setName(user.getName());
        r.setEmail(user.getEmail());
        r.setMobileNumber(user.getMobileNumber());
        r.setAddress(user.getAddress());
        r.setQualifications(user.getQualifications());
        r.setRole(user.getRole());
        r.setIsActive(user.getIsActive());
        r.setCreatedAt(user.getCreatedAt());
        return r;
    }

    // CONVERT JobPosting → DTO
    private JobPostingResponse toJobResponse(JobPosting job) {
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