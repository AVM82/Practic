package com.group.practic.security.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.practic.exception.Oauth2AuthenticationProcessingException;
import com.group.practic.service.PersonService;
import io.jsonwebtoken.lang.Assert;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private PersonService personService;

    @Autowired
    private Environment env;

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oauth2UserRequest)
            throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(oauth2UserRequest);
        try {
            Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());

            String accessToken = oauth2UserRequest.getAccessToken().getTokenValue();
            populateEmailAddressFromLinkedIn(accessToken, attributes);
            populateProfilePictureFromLinkedIn(accessToken, attributes);

            return personService.processUserRegistration(attributes, null, null);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new Oauth2AuthenticationProcessingException(ex.getMessage(), ex.getCause());
        }
    }

    private void populateEmailAddressFromLinkedIn(
            String accessToken, Map<String, Object> attributes
    ) throws OAuth2AuthenticationException {
        String emailEndpointUri = env.getProperty("linkedin.email-address-uri");
        Assert.notNull(emailEndpointUri, "LinkedIn email address endpoint required");
        
        ResponseEntity<String> response = makeLinkedInApiRequest(accessToken, emailEndpointUri);
        String element = extractElement("email", response.getBody());

        attributes.put("emailAddress", element);
    }

    private void populateProfilePictureFromLinkedIn(
            String accessToken, Map<String, Object> attributes) {
        String pictureEndpointUri = env.getProperty("linkedin.profile-picture-uri");
        Assert.notNull(pictureEndpointUri, "LinkedIn profile picture endpoint required");

        ResponseEntity<String> response = makeLinkedInApiRequest(accessToken, pictureEndpointUri);
        String element = extractElement("picture", response.getBody());
       
        attributes.put("pictureUrl", element);
    }

    private ResponseEntity<String> makeLinkedInApiRequest(
            String accessToken, String requestEndpoint) {
        // Create a header with the access token
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Make a GET request to LinkedIn's email endpoint with the token
        return restTemplate.exchange(requestEndpoint, HttpMethod.GET, entity, String.class);
    }

    private String extractElement(String element, String responseBody) {
        String result = null;
        try {
            // Parse the JSON response string into a JsonNode object
            JsonNode rootNode = objectMapper.readTree(responseBody);
            if (!rootNode.isEmpty()) {
                // Extract the element
                switch (element) {
                    case "email":
                        result = extractEmailAddressElement(rootNode);
                        break;
                    case "picture":
                        result = extractProfilePictureUrlElement(rootNode);
                        break;
                    default:
                        throw new IllegalArgumentException("No such known element");
                }
            }
        } catch (JsonProcessingException e) {
            log.error("Cannot process JSON from response's body", e);
        }
        
        return result;
    }

    private String extractEmailAddressElement(JsonNode rootNode) {
        JsonNode emailAddressElement = rootNode
                .get("elements").get(0)
                .get("handle~")
                .get("emailAddress");

        return emailAddressElement.textValue();
    }
    
    private String extractProfilePictureUrlElement(JsonNode rootNode) {
        JsonNode profilePictureUrlElement = rootNode
                .get("profilePicture")
                .get("displayImage~")
                .get("elements").get(0)
                .get("identifiers").get(0)
                .get("identifier");

        return profilePictureUrlElement.textValue();
    }
}
