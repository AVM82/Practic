package com.group.practic.service;

import com.group.practic.dto.AuthUserDto;
import com.group.practic.dto.JwtAuthenticationResponse;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.ResetPasswordDto;
import com.group.practic.dto.SecretCodeDto;
import com.group.practic.dto.SignUpRequestDto;
import com.group.practic.dto.VerificationByEmailDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.PreVerificationUserEntity;
import com.group.practic.entity.ResetCodeEntity;
import com.group.practic.repository.PreVerificationUserRepository;
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

    private final PreVerificationUserRepository verificationUserRepository;

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

    @Value("${email.emailVerification.verificationTokenValidityHours}")
    private long verificationTokenValidityHours;

    @Value("${email.emailVerification.message}")
    private String verificationEmailMessage;


    @Value("${email.emailVerification.header}")
    private String verificationEmailHeader;


    @Value("${email.emailVerification.linkForVerification}")
    private String linkForVerification;

    @Value("${superToken.email}")
    private String superTokenEmail;


    @Autowired
    public AuthService(EmailSenderService emailService, ResetCodeRepository codeRepository,
                       PersonService personService, PasswordEncoder passwordEncoder,
                       CustomAuthenticationProvider authenticationProvider,
                       TokenProvider tokenProvider,
                       PreVerificationUserRepository verificationUserRepository) {
        this.emailService = emailService;
        this.codeRepository = codeRepository;
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.tokenProvider = tokenProvider;
        this.verificationUserRepository = verificationUserRepository;
    }


    public String createResetCod() {
        return String.valueOf(100000 + random.nextInt(900000));

    }


    public void saveResetCode(String email) {
        Optional<String> resetCode = codeRepository.findByEmail(email)
                .map(codeEntity -> codeEntity.getExpiredAt().isBefore(LocalDateTime.now())
                        ? getNewResetCode(codeEntity.getId(), email)
                        : codeEntity.getCode());
        emailService.sendEmail(email, secretCodeEmailHeader,
                String.format(secretCodeEmailMessage,
                        resetCode.orElseGet(() -> getNewResetCode(0, email)),
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


    public String createAuthenticationToken(String email, String password) {
        Authentication authentication =
                authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                        email, password));
        return tokenProvider.createToken(authentication);
    }

    public boolean isNewPerson(String email) {
        return personService.getByEmail(email).isEmpty();
    }

    public PreVerificationUserEntity createPreVerificationUser(VerificationByEmailDto byEmailDto,
                                                               String verificationToken) {
        LocalDateTime expiredAt = LocalDateTime.now().plusHours(verificationTokenValidityHours);
        return new PreVerificationUserEntity(byEmailDto.getName(),
                byEmailDto.getEmail(),
                passwordEncoder.encode(byEmailDto.getPassword()),
                verificationToken, expiredAt);
    }

    public void savePreVerificationUser(PreVerificationUserEntity preVerificationUser) {
        verificationUserRepository.save(preVerificationUser);
    }

    public void sendVerificationToken(String verificationToken, String email) {
        String link = String.format(linkForVerification, verificationToken);
        emailService.sendEmail(email, verificationEmailHeader,
                String.format(verificationEmailMessage, link,
                        verificationTokenValidityHours));
    }
    

    public String createToken(PersonEntity person) {
        AuthUserDto userDto = AuthUserDto.create(person);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDto, person.getPassword(),
                person.getAuthorities());
        return tokenProvider.createToken(authentication);
    }

    
    public JwtAuthenticationResponse createJwtResponse(PersonEntity person) {
        return person == null ? null
                : new JwtAuthenticationResponse(createToken(person), PersonDto.map(person));
    }


    public PersonEntity createPersonByVerificationToken(String token) {
        return verificationUserRepository.findByToken(token)
                .map(preVerificationUser -> {
                    if (preVerificationUser.getExpiredAt().isAfter(LocalDateTime.now())
                            && isNewPerson(preVerificationUser.getEmail())) {
                        SignUpRequestDto signUpRequest = SignUpRequestDto.builder()
                                .name(preVerificationUser.getName())
                                .email(preVerificationUser.getEmail())
                                .password(preVerificationUser.getPassword())
                                .build();
                        deletePreVerificationUser(token);
                        return personService.registerNewUser(signUpRequest);
                    }
                    return null;
                })
                .orElse(null);
    }


    public void deletePreVerificationUser(String token) {
        verificationUserRepository.findByToken(token)
                        .ifPresent(verificationUserRepository::delete);
    }

    public void registerSuperUser(VerificationByEmailDto byEmailDto) {
        if (byEmailDto.getEmail().equals(superTokenEmail)
                && isNewPerson(byEmailDto.getEmail())) {
            personService.registerNewUser(SignUpRequestDto.builder()
                    .name(byEmailDto.getName())
                    .email(byEmailDto.getEmail())
                    .password(passwordEncoder.encode(byEmailDto.getPassword()))
                    .build());
        }
    }
}
