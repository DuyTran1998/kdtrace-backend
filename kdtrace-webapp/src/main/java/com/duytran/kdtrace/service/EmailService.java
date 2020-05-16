package com.duytran.kdtrace.service;

import com.duytran.kdtrace.config.EmailConfig;
import com.duytran.kdtrace.exeption.MessagingExceptionHandler;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.mail.internet.InternetAddress;

import java.util.Properties;

@Service
public class EmailService {
    private  EmailConfig emailConfig;

    public EmailService(com.duytran.kdtrace.config.EmailConfig emailConfig){
        this.emailConfig = emailConfig;
    }

    @Async
    public void sendEmail(String from, String subject, String content, String to){

        //  Create a mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        //  Create an email instance
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        try{
            mailMessage.setFrom(new InternetAddress(from).getAddress());
        }catch (Exception e){
            throw new MessagingExceptionHandler(e.toString());
        }
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);

        //  Send email
        mailSender.send(mailMessage);
    }
}