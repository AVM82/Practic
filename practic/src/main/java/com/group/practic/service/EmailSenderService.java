package com.group.practic.service;

import com.group.practic.dto.SendMessageDto;
import com.group.practic.util.AsyncThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailSenderService implements Sender {

    Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    private JavaMailSender mailSender;

    private ThreadGroup mailSenderThreadGroup = new ThreadGroup("Mail Sender");

    @Value("${MAIL_USERNAME}")
    private String sender;


    @Autowired
    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public boolean sendMessage(SendMessageDto messageDto) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(messageDto.getAddress());
            mailMessage.setText(messageDto.getMessage());
            mailMessage.setSubject(messageDto.getHeader());
            mailSender.send(mailMessage);
            logger.info("Email is sent to {}", messageDto.getAddress());
            return true;
        } catch (Exception e) {
            logger.error("Email is not sent to {}", messageDto.getAddress(), e);
        }
        return false;
    }


    public void sendEmail(String emailTo, String emailSubject, String emailText) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(emailSubject);
        mailMessage.setText(emailText);
        new AsyncThread<SimpleMailMessage>(
                this.mailSenderThreadGroup, emailTo, this::mailSender, mailMessage).start();
    }


    private void mailSender(SimpleMailMessage mailMessage) {
        try {
            mailSender.send(mailMessage);
        } catch (Exception e) {
            logger.error("Email is not sent to {}", mailMessage.getTo(), e);
        }
    }

}
