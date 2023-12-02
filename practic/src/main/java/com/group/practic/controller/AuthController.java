package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.AuthByEmailDto;
import com.group.practic.dto.JwtAuthenticationResponse;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.ResetPasswordDto;
import com.group.practic.dto.SecretCodeDto;
import com.group.practic.dto.VerificationByEmailDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.PreVerificationUserEntity;
import com.group.practic.service.AuthService;
import com.group.practic.service.PersonService;
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

    AuthService authService;

    PersonService personService;


    @Autowired
    public AuthController(PersonService personService, AuthService authService) {
        this.personService = personService;
        this.authService = authService;
    }


    @PostMapping("/auth")
    public ResponseEntity<JwtAuthenticationResponse> authUserByEmail(
            @RequestBody AuthByEmailDto byEmailDto) {
        return getResponse(personService.getByEmail(byEmailDto.getEmail())
                        .map(person -> new JwtAuthenticationResponse(
                                authService.createAuthenticationToken(byEmailDto.getEmail(),
                                        byEmailDto.getPassword()), PersonDto.map(person))));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUserByEmail(@RequestParam String verificationToken) {
        if (authService.isMatchVerificationToken(verificationToken)) {
            PersonEntity person = authService.createPersonByVerificationToken(verificationToken);
            authService.deletePreVerificationUser(verificationToken);
            String token = authService.createTokenForPerson(person);
            PersonDto userInfo = PersonDto.map(person);

            return ResponseEntity.ok(new JwtAuthenticationResponse(token, userInfo));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
    }


    @PostMapping("/verification")
    public ResponseEntity<Void> verificationByEmail(
            @RequestBody VerificationByEmailDto byEmailDto) {
        if (authService.isNewPerson(byEmailDto.getEmail())) {
            String verificationToken = authService
                    .createTokenForPerson(new PersonEntity(byEmailDto.getName(), "", null));

            PreVerificationUserEntity preVerificationUser =
                    authService.createPreVerificationUser(byEmailDto, verificationToken);

            authService.savePreVerificationUser(preVerificationUser);

            authService.sendVerificationToken(verificationToken, byEmailDto.getEmail());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
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
