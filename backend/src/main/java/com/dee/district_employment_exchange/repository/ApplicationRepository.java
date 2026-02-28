package com.dee.district_employment_exchange.repository;

import com.dee.district_employment_exchange.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Get all applications by a job seeker
    List<Application> findByJobSeekerId(Long jobSeekerId);

    // Get all applications for a specific job
    List<Application> findByJobPostingId(Long jobPostingId);

    // Check if job seeker already applied for this job
    boolean existsByJobSeekerIdAndJobPostingId(
            Long jobSeekerId, Long jobPostingId);

    // Find specific application
    Optional<Application> findByJobSeekerIdAndJobPostingId(
            Long jobSeekerId, Long jobPostingId);

    long countByStatus(Application.Status status);
}