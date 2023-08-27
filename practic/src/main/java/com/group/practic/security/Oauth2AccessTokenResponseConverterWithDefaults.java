package com.group.practic.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class Oauth2AccessTokenResponseConverterWithDefaults implements
        Converter<Map<String, Object>, OAuth2AccessTokenResponse> {

    private static final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES = Stream
            .of(
                    OAuth2ParameterNames.ACCESS_TOKEN,
                    OAuth2ParameterNames.TOKEN_TYPE,
                    OAuth2ParameterNames.EXPIRES_IN,
                    OAuth2ParameterNames.REFRESH_TOKEN,
                    OAuth2ParameterNames.SCOPE)
            .collect(Collectors.toSet());

    @Override
    public OAuth2AccessTokenResponse convert(Map<String, Object> source) {
        OAuth2AccessToken.TokenType accessTokenType;
        accessTokenType = OAuth2AccessToken.TokenType.BEARER;
        Integer expiresIn = 0;
        log.debug("Token expires in = {}", source.get(OAuth2ParameterNames.EXPIRES_IN));

        String accessToken = (String) source.get(OAuth2ParameterNames.ACCESS_TOKEN);
        if (!accessToken.isEmpty()) {
            try {
                expiresIn = (Integer) source.get(OAuth2ParameterNames.EXPIRES_IN);
            } catch (NumberFormatException ex) {
                log.error("Number format exception in token.");
            }

            Set<String> scopes = Collections.emptySet();
            if (source.containsKey(OAuth2ParameterNames.SCOPE)) {
                String scope = (String) source.get(OAuth2ParameterNames.SCOPE);
                scopes = Arrays.stream(StringUtils.delimitedListToStringArray(scope, " "))
                        .collect(Collectors.toSet());
            }

            Map<String, Object> additionalParameters = new LinkedHashMap<>();
            source.entrySet()
                    .stream().filter(e -> !TOKEN_RESPONSE_PARAMETER_NAMES.contains(e.getKey()))
                    .forEach(e -> additionalParameters.put(e.getKey(), e.getValue()));

            return OAuth2AccessTokenResponse
                    .withToken(accessToken)
                    .tokenType(accessTokenType)
                    .expiresIn(expiresIn)
                    .scopes(scopes)
                    .additionalParameters(additionalParameters)
                    .build();
        }
        return null;
    }
}
