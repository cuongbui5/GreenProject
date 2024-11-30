package com.example.greenproject.service;

import com.example.greenproject.dto.MailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendEmailMessage(MailBody mailBody){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailBody.getTo());
        mailMessage.setFrom("nguyendanglinhaz@gmail.com");
        mailMessage.setSubject(mailBody.getSubject());
        mailMessage.setText(mailBody.getText());

        javaMailSender.send(mailMessage);
    }
}
