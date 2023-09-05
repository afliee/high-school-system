package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.util.mail.EmailDetails;
import com.highschool.highschoolsystem.util.mail.MailSender;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
//@RequiredArgsConstructor
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Autowired
    private ThymeLeafService thymeLeafService;
    @Autowired
    private AssignmentEmailService assignmentEmailService;

    @Autowired
    private MailSender mailSender;


    @Override
    public void sendEmail(EmailDetails emailDetails) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", mailSender.getAuth());
        properties.put("mail.smtp.starttls.enable", mailSender.getStarttls());
        properties.put("mail.smtp.host", mailSender.getHost());
        properties.put("mail.smtp.port", mailSender.getPort());

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailSender.getUsername(), mailSender.getPassword());
            }
        });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(mailSender.getUsername()));
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[] {
               new InternetAddress(emailDetails.getTo())
            });
            message.setSubject(emailDetails.getSubject());
            message.setContent(thymeLeafService.getContent(emailDetails), "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendAssignmentEmail(EmailDetails emailDetails, Object content) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", mailSender.getAuth());
        properties.put("mail.smtp.starttls.enable", mailSender.getStarttls());
        properties.put("mail.smtp.host", mailSender.getHost());
        properties.put("mail.smtp.port", mailSender.getPort());

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailSender.getUsername(), mailSender.getPassword());
            }
        });
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(mailSender.getUsername()));
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[] {
               new InternetAddress(emailDetails.getTo())
            });
            message.setSubject(emailDetails.getSubject());
            message.setContent(assignmentEmailService.getContent(emailDetails, content), "text/html; charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
