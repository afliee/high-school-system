package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.entity.Submitting;
import com.highschool.highschoolsystem.util.FileUploadUtils;
import com.highschool.highschoolsystem.util.mail.EmailDetails;
import com.highschool.highschoolsystem.util.mail.MailSender;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.File;
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
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{
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
        properties.put("mail.smtp.starttls.enable", true);
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
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{
                    new InternetAddress(emailDetails.getTo())
            });
            message.setSubject(emailDetails.getSubject());
//            message.setContent(assignmentEmailService.getContent(emailDetails, content), "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(assignmentEmailService.getContent(emailDetails, content), "text/html; charset=utf-8");
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File(FileUploadUtils.ASSIGNMENT_UPLOAD_DIR + File.separator + emailDetails.getPathToAttachment()));


            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendAssignmentGradingEmail(EmailDetails emailDetails, Object content) {
        if (!(content instanceof Submitting)) {
            throw new RuntimeException("Content must be instance of Submitting");
        }

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
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{
                    new InternetAddress(emailDetails.getTo())
            });
            message.setSubject(emailDetails.getSubject());
            message.setContent(assignmentEmailService.getContent(emailDetails, (Submitting) content), "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException exception) {
            exception.printStackTrace();
        }
    }
}
