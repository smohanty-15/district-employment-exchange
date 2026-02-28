package com.dee.district_employment_exchange.repository;

import com.dee.district_employment_exchange.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    // Get all jobs by a specific employer
    List<JobPosting> findByEmployerId(Long employerId);

    // Get all active jobs
    List<JobPosting> findByStatus(JobPosting.Status status);

    // Count by status
    long countByStatus(JobPosting.Status status);

    // Get all active jobs by location
    List<JobPosting> findByStatusAndLocationContainingIgnoreCase(
            JobPosting.Status status, String location);

    // Get all active jobs by title keyword
    List<JobPosting> findByStatusAndTitleContainingIgnoreCase(
            JobPosting.Status status, String title);

    // Search with filters - all optional
    @Query("SELECT j FROM JobPosting j WHERE " +
            "j.status = 'ACTIVE' AND " +
            "(:keyword IS NULL OR " +
            "  LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "  LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:location IS NULL OR " +
            "  LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:jobType IS NULL OR j.jobType = :jobType)")
    List<JobPosting> searchJobs(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("jobType") JobPosting.JobType jobType);
}