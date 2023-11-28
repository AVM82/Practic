package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.JwtAuthenticationResponse;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.RegisterByEmailDto;
import com.group.practic.dto.ResetPasswordDto;
import com.group.practic.dto.SecretCodeDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.service.AuthService;
import com.group.practic.service.PersonService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    AuthService authService;

    PersonService personService;


    @Autowired
    public AuthController(PersonService personService, AuthService authService) {
        this.personService = personService;
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> registerUserByEmail(
            @RequestBody RegisterByEmailDto byEmailDto) {
        PersonEntity person = authService.registerNewUserByEmail(byEmailDto);
        return getResponse(Optional.ofNullable(
                person != null
                        ? new JwtAuthenticationResponse(authService.createToken(byEmailDto),
                                PersonDto.map(person))
                        : null));
    }


    @PostMapping("/password-reset/send-code")
    public ResponseEntity<Void> sendSecretCodeForPasswordReset(
            @RequestParam("email") String email) {
        if (!personService.existByEmail(email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        authService.saveResetCode(email);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/password-reset/match-code")
    public ResponseEntity<Void> matchSecretCode(@RequestBody SecretCodeDto codeDto) {
        return authService.isMatchSecretCode(codeDto) ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }


    @PatchMapping("/password-reset")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDto passwordDto) {
        if (authService
                .isMatchSecretCode(new SecretCodeDto(passwordDto.code(), passwordDto.email()))) {
            authService.changePassword(passwordDto);
        } else {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
