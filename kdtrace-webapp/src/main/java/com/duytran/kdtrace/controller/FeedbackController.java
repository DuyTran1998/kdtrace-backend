package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.config.EmailConfig;
import com.duytran.kdtrace.model.Feedback;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;
import java.util.Properties;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private EmailConfig emailConfig;

    public FeedbackController(EmailConfig emailConfig){
        this.emailConfig = emailConfig;
    }

    @PostMapping
    public void sendFeedBack(@RequestBody Feedback feedback, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationException("Feedback is not valid");
        }

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
        mailMessage.setFrom(feedback.getEmail());
        mailMessage.setTo("16521659@gm.uit.edu.vn");
        mailMessage.setSubject("New feedback" + feedback.getName());
        mailMessage.setText(feedback.getFeedback());

        //  Send email
        mailSender.send(mailMessage);
    }
}