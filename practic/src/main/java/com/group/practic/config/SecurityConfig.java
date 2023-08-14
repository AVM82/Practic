package com.group.practic.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/**"))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/login", "/api/course",
                                "/*.js", "/*.html", "/*.css", "/*.woff2", "/*.ico")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login").permitAll())
                .oauth2Login(oauth ->
                        oauth
                                .tokenEndpoint(token -> {
                                    DefaultMapOAuth2AccessTokenResponseConverter defaultMapConverter
                                            = new DefaultMapOAuth2AccessTokenResponseConverter();

                                    Converter<Map<String, Object>, OAuth2AccessTokenResponse>
                                            linkedinMapConverter = tokenResponse -> {
                                                Map<String, Object> withTokenType =
                                                        new HashMap<>(tokenResponse);
                                                withTokenType.put(OAuth2ParameterNames.TOKEN_TYPE,
                                                        OAuth2AccessToken
                                                                .TokenType.BEARER.getValue());
                                                return defaultMapConverter.convert(withTokenType);
                                            };

                                    OAuth2AccessTokenResponseHttpMessageConverter httpConverter =
                                            new OAuth2AccessTokenResponseHttpMessageConverter();
                                    httpConverter
                                            .setAccessTokenResponseConverter(linkedinMapConverter);

                                    RestTemplate restOperations = new RestTemplate(
                                            List.of(new FormHttpMessageConverter(),
                                                    httpConverter));
                                    restOperations.setErrorHandler(
                                            new OAuth2ErrorResponseErrorHandler());

                                    DefaultAuthorizationCodeTokenResponseClient client =
                                            new DefaultAuthorizationCodeTokenResponseClient();
                                    client.setRestOperations(restOperations);

                                    token.accessTokenResponseClient(client);
                                }));

        return http.build();
    }
}