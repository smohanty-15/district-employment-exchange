package com.dee.district_employment_exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DistrictEmploymentExchangeApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				DistrictEmploymentExchangeApplication.class, args);
	}
}
