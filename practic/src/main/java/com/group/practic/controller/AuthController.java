package com.group.practic.controller;

import com.group.practic.dto.JwtAuthenticationResponse;
import com.group.practic.dto.RegisterByEmailDto;
import com.group.practic.dto.SignUpRequestDto;
import com.group.practic.dto.UserInfoDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.security.CustomAuthenticationProvider;
import com.group.practic.security.Oauth2AuthenticationSuccessHandler;
import com.group.practic.security.jwt.TokenProvider;
import com.group.practic.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    PersonService personService;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;


    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerUserByEmail(@RequestBody RegisterByEmailDto byEmailDto) {
        String email = byEmailDto.getEmail();
        String password = passwordEncoder.encode(byEmailDto.getPassword());
        PersonEntity person = personService.loadUserByEmail(email);
        if (person == null) {
            person = personService.registerNewUser(
                    SignUpRequestDto.builder().name(byEmailDto.getName()).email(email)
                            .password(password).build());
        }

        if (person != null) {
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            byEmailDto.getEmail(),
                            byEmailDto.getPassword()
                    )
            );

            UserInfoDto userInfo = new UserInfoDto(
                    String.valueOf(person.getId()), person.getName(),
                    person.getEmail(), person.getRoles().stream()
                    .map(RoleEntity::getName).toList());
            String token = tokenProvider.createToken(authentication);

            return ResponseEntity.ok(new JwtAuthenticationResponse(token, userInfo));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User registration failed");
        }
    }
}
