package com.group.practic.security;

import com.group.practic.dto.AuthUserDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private final PersonService personService;

    Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    @Autowired
    PasswordEncoder passwordEncoder;
    public CustomAuthenticationProvider(PersonService personService) {
        this.personService = personService;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        logger.info("authenticate start email: {}, password: {}",email,password);

        PersonEntity userDetails = personService.loadUserByEmail(email);
        logger.info("user name: {}",userDetails.getName());
        logger.info("person password: {} ",userDetails.getPassword());
        logger.info("person password: {} ",password);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            logger.info("прошли if");

            AuthUserDto userDto = AuthUserDto.create(userDetails);
            logger.info("userdto create: {}",userDto);
            return new UsernamePasswordAuthenticationToken(userDto, password, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
