package com.group.practic.security;

import static com.group.practic.security.HttpCookieOauth2AuthRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.group.practic.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class Oauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final HttpCookieOauth2AuthRequestRepository httpCookieOauth2AuthRequestRepository;


    @Autowired
    public Oauth2AuthenticationFailureHandler(
            HttpCookieOauth2AuthRequestRepository httpCookieOauth2AuthRequestRepository) {
        this.httpCookieOauth2AuthRequestRepository = httpCookieOauth2AuthRequestRepository;
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue).orElse(("/"));

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage()).build().encode()
                .toUriString();

        httpCookieOauth2AuthRequestRepository.removeAuthorizationRequestCookies(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
