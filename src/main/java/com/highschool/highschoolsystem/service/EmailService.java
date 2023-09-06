package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.util.mail.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
    void sendAssignmentEmail(EmailDetails emailDetails, Object content);
    void sendAssignmentGradingEmail(EmailDetails emailDetails, Object content);
}
