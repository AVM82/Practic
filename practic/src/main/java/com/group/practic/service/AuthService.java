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

    @Value("${email.secretCode.message}")
    private String secretCodeEmailMessage;

    @Value("${email.secretCode.header}")
    private String secretCodeEmailHeader;

    @Value("${email.secretCode.codeValidityMinutes}")
    private long codeValidityMinutes;
    
    Random random = new Random();


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


    public String createResetCod() {
        int resetCode = 100000 + random.nextInt(900000);
        return String.valueOf(resetCode);

    }


    public void saveResetCode(String email) {
        Optional<String> resetCode = codeRepository.findByEmail(email)
                .map(codeEntity -> codeEntity.getExpiredAt().isBefore(LocalDateTime.now())
                        ? getNewResetCode(codeEntity.getId(), email)
                        : codeEntity.getCode());
        emailService.sendEmail(email, secretCodeEmailHeader,
                String.format(secretCodeEmailMessage,
                        resetCode.isPresent() ? resetCode.get() : getNewResetCode(0, email),
                                codeValidityMinutes));
    }


    protected String getNewResetCode(long id, String email) {
        String resetCode = createResetCod();
        codeRepository.save(new ResetCodeEntity(id, email, resetCode, codeValidityMinutes));
        return resetCode;
    }


    public boolean isMatchSecretCode(SecretCodeDto passwordDto) {
        return codeRepository.findByEmail(passwordDto.email())
                .map(resetCode -> resetCode.getExpiredAt().isAfter(LocalDateTime.now()))
                .orElse(false);
    }


    public void changePassword(ResetPasswordDto passwordDto) {
        personService.getByEmail(passwordDto.email()).ifPresent(person -> {
            person.setPassword(passwordEncoder.encode(passwordDto.newPassword()));
            personService.save(person);
        });
    }


    public PersonEntity registerNewUserByEmail(RegisterByEmailDto byEmailDto) {
        Optional<PersonEntity> person = personService.getByEmail(byEmailDto.getEmail());
        return person.isPresent() ? person.get()
                : personService.registerNewUser(SignUpRequestDto.builder()
                        .name(byEmailDto.getName()).email(byEmailDto.getEmail())
                        .password(passwordEncoder.encode(byEmailDto.getPassword())).build());
    }


    public String createToken(RegisterByEmailDto byEmailDto) {
        Authentication authentication =
                authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                        byEmailDto.getEmail(), byEmailDto.getPassword()));
        return tokenProvider.createToken(authentication);
    }

}
