package com.group.practic.service;

import com.group.practic.dto.RegisterByEmailDto;
import com.group.practic.dto.ResetPasswordDto;
import com.group.practic.dto.SecretCodeDto;
import com.group.practic.dto.SignUpRequestDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ResetCodeEntity;
import com.group.practic.repository.ResetCodeRepository;
import com.group.practic.security.CustomAuthenticationProvider;
import com.group.practic.security.jwt.TokenProvider;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final ResetCodeRepository codeRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailSenderService emailService;

    private final PersonService personService;

    private final CustomAuthenticationProvider authenticationProvider;

    private final TokenProvider tokenProvider;

    private final Long codeValidityTime = 5L;

    @Value("${email.secretCode.message}")
    private String secretCodeEmailMessage;

    @Value("${email.secretCode.header}")
    private String secretCodeEmailHeader;


    @Autowired
    public AuthService(EmailSenderService emailService, ResetCodeRepository codeRepository,
            PersonService personService, PasswordEncoder passwordEncoder,
            CustomAuthenticationProvider authenticationProvider, TokenProvider tokenProvider) {
        this.emailService = emailService;
        this.codeRepository = codeRepository;
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.tokenProvider = tokenProvider;
    }


    public String createResetCode() {
        Random random = new Random();
        int resetCode = 100000 + random.nextInt(900000);
        return String.valueOf(resetCode);

    }


    public void saveResetCode(String resetCode, PersonEntity person) {
        String currentResetCode = resetCode;
        if (resetCode != null && person != null) {
            ResetCodeEntity entity = codeRepository.findByPersonEmail(person.getEmail());
            if (entity != null) {
                LocalDateTime timeEntity = entity.getExpiredAt();
                if (timeEntity.isBefore(LocalDateTime.now())) {
                    codeRepository.deleteById(entity.getId());
                    codeRepository.save(new ResetCodeEntity(person, resetCode,
                            LocalDateTime.now().plusMinutes(codeValidityTime)));
                }
                currentResetCode = entity.getCode();
            } else {
                codeRepository.save(new ResetCodeEntity(person, resetCode,
                        LocalDateTime.now().plusMinutes(codeValidityTime)));
            }
            sendEmailAuthMessage(currentResetCode, person.getEmail(), secretCodeEmailMessage,
                    secretCodeEmailHeader);
        }
    }


    public void sendEmailAuthMessage(String authInfo, String email, String message, String header) {
        String emailMessage = String.format(message, authInfo);
        emailService.sendEmail(email, emailMessage, header);
    }


    public boolean isMatchSecretCode(SecretCodeDto passwordDto) {
        ResetCodeEntity resetCode = codeRepository.findByPersonEmail(passwordDto.email());
        return resetCode != null && resetCode.getExpiredAt().isAfter(LocalDateTime.now());
    }


    public void changePassword(ResetPasswordDto passwordDto) {
        Optional<PersonEntity> currentPerson = personService.getByEmail(passwordDto.email());
        if (currentPerson.isPresent()) {
            currentPerson.get().setPassword(encodePass(passwordDto.newPassword()));
            personService.save(currentPerson.get());
        }
    }


    public void deleteCodeEntity(ResetPasswordDto passwordDto) {
        ResetCodeEntity entity = codeRepository.findByCode(passwordDto.code());
        if (entity != null) {
            codeRepository.delete(entity);
        }
    }


    public String encodePass(String password) {
        return passwordEncoder.encode(password);
    }


    public PersonEntity registerNewUserByEmail(RegisterByEmailDto byEmailDto) {
        Optional<PersonEntity> person = personService.getByEmail(byEmailDto.getEmail());
        return person.isPresent() ? person.get() 
                : personService.registerNewUser(SignUpRequestDto.builder()
                        .name(byEmailDto.getName())
                        .email(byEmailDto.getEmail())
                        .password(encodePass(byEmailDto.getPassword()))
                        .build());
    }


    public String createToken(RegisterByEmailDto byEmailDto) {
        Authentication authentication =
                authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                        byEmailDto.getEmail(), byEmailDto.getPassword()));
        return tokenProvider.createToken(authentication);
    }

}
