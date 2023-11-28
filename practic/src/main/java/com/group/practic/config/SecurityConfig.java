package com.group.practic.config;

import com.group.practic.security.HttpCookieOauth2AuthRequestRepository;
import com.group.practic.security.Oauth2AccessTokenResponseConverterWithDefaults;
import com.group.practic.security.Oauth2AuthenticationFailureHandler;
import com.group.practic.security.Oauth2AuthenticationSuccessHandler;
import com.group.practic.security.jwt.TokenAuthenticationFilter;
import com.group.practic.security.user.CustomOauth2UserService;
import com.group.practic.security.user.CustomOidcUserService;
import com.group.practic.service.PersonService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${logout.successRedirectUris}")
    private String logoutRedirectUri;


    private CustomOidcUserService getCustomOidcUserService() {
        return applicationContext.getBean(CustomOidcUserService.class);
    }


    private CustomOauth2UserService getCustomOauth2UserService() {
        return applicationContext.getBean(CustomOauth2UserService.class);
    }


    private Oauth2AuthenticationFailureHandler getOauth2AuthenticationFailureHandler() {
        return applicationContext.getBean(Oauth2AuthenticationFailureHandler.class);
    }


    private Oauth2AuthenticationSuccessHandler getOauth2AuthenticationSuccessHandler() {
        return applicationContext.getBean(Oauth2AuthenticationSuccessHandler.class);
    }

    @Autowired
    private PersonService personService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personService).passwordEncoder(passwordEncoder());
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable).cors(CorsConfigurer::disable).sessionManagement(
                httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(FormLoginConfigurer::disable).httpBasic(HttpBasicConfigurer::disable)
                .exceptionHandling(
                        httpSecurityExceptionHandlingConfigurer ->
                                httpSecurityExceptionHandlingConfigurer
                                        .authenticationEntryPoint(
                                                new RestAuthenticationEntryPoint()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/feedbacks", "/api/register",
                                "/api/auth",
                                "/api/password-reset/send-code", "/api/password-reset/match-code",
                                "/api/password-reset",
                                "api/verification")
                        .permitAll().requestMatchers("/api/**").authenticated().anyRequest()
                        .permitAll())
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(
                                endpointConfig -> endpointConfig.authorizationRequestRepository(
                                        new HttpCookieOauth2AuthRequestRepository()))
                        .userInfoEndpoint(userInfoConfig -> userInfoConfig
                                .oidcUserService(getCustomOidcUserService())
                                .userService(getCustomOauth2UserService()))
                        .tokenEndpoint(tokenConfig -> tokenConfig
                                .accessTokenResponseClient(authorizationCodeTokenResponseClient()))
                        .successHandler(getOauth2AuthenticationSuccessHandler())
                        .failureHandler(getOauth2AuthenticationFailureHandler()))
                .addFilterBefore(tokenAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutSuccessUrl(logoutRedirectUri).clearAuthentication(true));

        return http.build();
    }


    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    /*
     * By default, Spring Oauth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save the
     * authorization request. But, since our service is stateless, we can't save it in the session.
     * We'll save the request in a Base64 encoded cookie instead.
     */


    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> 
            authorizationCodeTokenResponseClient() {
        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(
                new Oauth2AccessTokenResponseConverterWithDefaults());

        RestTemplate restTemplate = new RestTemplate(
                Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));

        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();

        tokenResponseClient.setRestOperations(restTemplate);

        return tokenResponseClient;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
