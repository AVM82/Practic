package com.group.practic.controller;

import com.group.practic.dto.JwtAuthenticationResponse;
import com.group.practic.dto.RegisterByEmailDto;
import com.group.practic.dto.ResetPasswordDto;
import com.group.practic.dto.SecretCodeDto;
import com.group.practic.dto.UserInfoDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
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
    public ResponseEntity<?> registerUserByEmail(@RequestBody RegisterByEmailDto byEmailDto) {
        PersonEntity person = authService.registerNewUserByEmail(byEmailDto);
        if (person != null) {
            String token = authService.createToken(byEmailDto);

            UserInfoDto userInfo = new UserInfoDto(String.valueOf(person.getId()), person.getName(),
                    person.getEmail(),
                    person.getRoles().stream().map(RoleEntity::getName).toList());

            return ResponseEntity.ok(new JwtAuthenticationResponse(token, userInfo));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
        }
    }


    @PostMapping("/password-reset/send-code")
    public ResponseEntity<Void> sendSecretCodeForPasswordReset(
            @RequestParam("email") String email) {
        Optional<PersonEntity> person = personService.getByEmail(email);
        if (person.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String resetCode = authService.createResetCode();
        authService.saveResetCode(resetCode, person.get());

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
            authService.deleteCodeEntity(passwordDto);
        } else {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
