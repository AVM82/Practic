package com.group.practic.service;

import com.group.practic.dto.SendMessageDto;
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

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public boolean sendMessage(SendMessageDto messageDto) {
        try {
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(messageDto.getAddress());
            mailMessage.setText(messageDto.getMessage());
            mailMessage.setSubject(messageDto.getHeader());

            mailSender.send(mailMessage);
            logger.info("Yor email is sent to {}", messageDto.getAddress());
            return true;
        } catch (Exception e) {
            logger.error("Yor email is not sent to {}", messageDto.getAddress(), e);
        }
        return false;
    }
}
