package com.group.practic.security.user;

import com.group.practic.exception.Oauth2AuthenticationProcessingException;
import com.group.practic.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private PersonService personService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        try {
            return personService.processUserRegistration(
                    oidcUser.getAttributes(),
                    oidcUser.getIdToken(),
                    oidcUser.getUserInfo()
            );
        } catch (AuthenticationException ex) {
            log.error("AuthenticationException ", ex);
        } catch (Exception ex) {
            log.error("Exception in auth", ex);
            // Throwing an instance of AuthenticationException will trigger the
            // OAuth2AuthenticationFailureHandler
            throw new Oauth2AuthenticationProcessingException(ex.getMessage(), ex.getCause());
        }
        return null;
    }
}

