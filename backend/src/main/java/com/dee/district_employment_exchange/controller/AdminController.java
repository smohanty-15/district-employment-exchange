package com.dee.district_employment_exchange.controller;

import com.dee.district_employment_exchange.dto.AdminDashboardResponse;
import com.dee.district_employment_exchange.dto.ApiResponse;
import com.dee.district_employment_exchange.dto.JobPostingResponse;
import com.dee.district_employment_exchange.dto.UserResponse;
import com.dee.district_employment_exchange.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Dashboard stats
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>>
    getDashboard() {
        AdminDashboardResponse dashboard = adminService.getDashboard();
        return ResponseEntity.ok(
                ApiResponse.success("Dashboard retrieved", dashboard));
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>>
    getAllUsers() {
        List<UserResponse> users = adminService.getAllUsers();
        return ResponseEntity.ok(
                ApiResponse.success("Users retrieved", users));
    }

    // Toggle user active/inactive
    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<ApiResponse<UserResponse>>
    toggleUserStatus(@PathVariable Long id) {
        UserResponse user = adminService.toggleUserStatus(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "User status updated successfully", user));
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<?>>
    deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(
                ApiResponse.success("User deleted successfully"));
    }

    // Get all jobs
    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>>
    getAllJobs() {
        List<JobPostingResponse> jobs = adminService.getAllJobs();
        return ResponseEntity.ok(
                ApiResponse.success("Jobs retrieved", jobs));
    }

    // Suspend a job
    @PutMapping("/jobs/{id}/suspend")
    public ResponseEntity<ApiResponse<JobPostingResponse>>
    suspendJob(@PathVariable Long id) {
        JobPostingResponse job = adminService.suspendJob(id);
        return ResponseEntity.ok(
                ApiResponse.success("Job suspended successfully", job));
    }
}