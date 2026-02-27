package com.dee.district_employment_exchange.repository;

import com.dee.district_employment_exchange.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    // Get all jobs by a specific employer
    List<JobPosting> findByEmployerId(Long employerId);

    // Get all active jobs
    List<JobPosting> findByStatus(JobPosting.Status status);

    // Get all active jobs by location
    List<JobPosting> findByStatusAndLocationContainingIgnoreCase(
            JobPosting.Status status, String location);

    // Get all active jobs by title keyword
    List<JobPosting> findByStatusAndTitleContainingIgnoreCase(
            JobPosting.Status status, String title);
}