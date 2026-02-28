package com.dee.district_employment_exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name}")
    private String appName;

    // SEND ANY EMAIL
    @Async
    public void sendEmail(String toEmail,
                          String subject,
                          String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}",
                    toEmail, e.getMessage());
        }
    }

    // WELCOME EMAIL
    @Async
    public void sendWelcomeEmail(String toEmail, String userName) {
        String subject = "Welcome to " + appName + "!";
        String body = "Dear " + userName + ",\n\n" +
                "Welcome to " + appName + "!\n\n" +
                "Your account has been created successfully.\n\n" +
                "You can now:\n" +
                "- Search for jobs\n" +
                "- Apply for positions\n" +
                "- Track your applications\n\n" +
                "Best regards,\n" +
                appName + " Team";
        sendEmail(toEmail, subject, body);
    }

    // APPLICATION CONFIRMATION (to job seeker)
    @Async
    public void sendApplicationConfirmation(String toEmail,
                                            String userName,
                                            String jobTitle,
                                            String companyName) {
        String subject = "Application Submitted - " + jobTitle;
        String body = "Dear " + userName + ",\n\n" +
                "Your application has been submitted successfully!\n\n" +
                "Job Title: " + jobTitle + "\n" +
                "Company: " + companyName + "\n\n" +
                "We will notify you when the employer reviews " +
                "your application.\n\n" +
                "Best regards,\n" +
                appName + " Team";
        sendEmail(toEmail, subject, body);
    }

    // NEW APPLICATION ALERT (to employer)
    @Async
    public void sendNewApplicationAlert(String toEmail,
                                        String employerName,
                                        String applicantName,
                                        String jobTitle) {
        String subject = "New Application Received - " + jobTitle;
        String body = "Dear " + employerName + ",\n\n" +
                "You have received a new application!\n\n" +
                "Job Title: " + jobTitle + "\n" +
                "Applicant: " + applicantName + "\n\n" +
                "Login to your dashboard to review the application.\n\n" +
                "Best regards,\n" +
                appName + " Team";
        sendEmail(toEmail, subject, body);
    }

    // STATUS UPDATE EMAIL
    // (to job seeker)
    @Async
    public void sendStatusUpdateEmail(String toEmail,
                                      String userName,
                                      String jobTitle,
                                      String newStatus,
                                      String employerNotes) {
        String subject = "Application Update - " + jobTitle;

        String statusMessage;
        switch (newStatus) {
            case "UNDER_REVIEW":
                statusMessage = "Your application is now under review.";
                break;
            case "SHORTLISTED":
                statusMessage = "Congratulations! You have been shortlisted.";
                break;
            case "SELECTED":
                statusMessage = "Congratulations! You have been SELECTED for this position!";
                break;
            case "REJECTED":
                statusMessage = "Thank you for applying. Unfortunately, you were not selected this time.";
                break;
            default:
                statusMessage = "Your application status has been updated to: " + newStatus;
        }

        String body = "Dear " + userName + ",\n\n" +
                "Your application status has been updated.\n\n" +
                "Job Title: " + jobTitle + "\n" +
                "Status: " + newStatus + "\n\n" +
                statusMessage + "\n\n" +
                (employerNotes != null && !employerNotes.isEmpty()
                        ? "Message from employer: " + employerNotes + "\n\n"
                        : "") +
                "Best regards,\n" +
                appName + " Team";

        sendEmail(toEmail, subject, body);
    }
}