package com.group.practic.security.user;

import com.group.practic.exception.Oauth2AuthenticationProcessingException;
import com.group.practic.service.PersonService;
import io.jsonwebtoken.lang.Assert;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private PersonService personService;

    @Autowired
    private Environment env;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oauth2UserRequest)
            throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(oauth2UserRequest);
        try {
            Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
            populateEmailAddressFromLinkedIn(oauth2UserRequest, attributes);
            return personService.processUserRegistration(attributes, null, null);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new Oauth2AuthenticationProcessingException(ex.getMessage(), ex.getCause());
        }
    }

    public void populateEmailAddressFromLinkedIn(
            OAuth2UserRequest oauth2UserRequest, Map<String, Object> attributes
    ) throws OAuth2AuthenticationException {
        String emailEndpointUri = env.getProperty("linkedin.email-address-uri");
        Assert.notNull(emailEndpointUri, "LinkedIn email address end point required");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + oauth2UserRequest.getAccessToken().getTokenValue());
        HttpEntity<?> entity = new HttpEntity<>("", headers);

        ResponseEntity<Map> response = restTemplate
                .exchange(emailEndpointUri, HttpMethod.GET, entity, Map.class);

        if (response.getBody() != null) {
            List<?> list = (List<?>) response.getBody().get("elements");
            Map map = (Map<?, ?>) ((Map<?, ?>) list.get(0)).get("handle~");
            attributes.putAll(map);
        }

    }
}
