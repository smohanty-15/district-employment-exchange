package com.dee.district_employment_exchange.dto;

import com.dee.district_employment_exchange.entity.User;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String mobileNumber;
    private String address;
    private String qualifications;
    private User.Role role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
