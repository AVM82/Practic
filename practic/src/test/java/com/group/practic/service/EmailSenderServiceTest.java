package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.group.practic.dto.SendMessageDto;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

class EmailSenderServiceTest {
    @InjectMocks
    private EmailSenderService emailSenderService;

    @Mock
    private JavaMailSender mailSender;
    @Mock
    private Logger logger;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMessage() {

        SendMessageDto messageDto = new SendMessageDto();
        messageDto.setAddress("recipient@example.com");
        messageDto.setHeader("Test Header");
        messageDto.setMessage("Test Message");

        SimpleMailMessage expectedMailMessage = new SimpleMailMessage();
        expectedMailMessage.setFrom("your_sender_email@example.com");
        expectedMailMessage.setTo(messageDto.getAddress());
        expectedMailMessage.setSubject(messageDto.getHeader());
        expectedMailMessage.setText(messageDto.getMessage());

        Mockito.when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        Mockito.doAnswer((Answer<Void>) invocation -> null).when(logger).info(Mockito.anyString());
        boolean result = emailSenderService.sendMessage(messageDto);

        assertTrue(result);
        Mockito.verify(mailSender).send(Mockito.any(SimpleMailMessage.class));
    }
}