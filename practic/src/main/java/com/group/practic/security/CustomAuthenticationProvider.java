package com.group.practic.security;

import com.group.practic.dto.AuthUserDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private final PersonService personService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        PersonEntity userDetails = personService.loadUserByEmail(email);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            AuthUserDto userDto = AuthUserDto.create(userDetails);
            return new UsernamePasswordAuthenticationToken(
                    userDto, password, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
