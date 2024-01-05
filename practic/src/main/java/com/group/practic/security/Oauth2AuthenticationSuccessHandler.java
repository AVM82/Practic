package com.group.practic.security;

import static com.group.practic.security.HttpCookieOauth2AuthRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.group.practic.config.AppProperties;
import com.group.practic.exception.BadRequestException;
import com.group.practic.security.jwt.TokenProvider;
import com.group.practic.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private TokenProvider tokenProvider;

    private AppProperties appProperties;

    private HttpCookieOauth2AuthRequestRepository httpCookieOauth2AuthRequestRepository;


    @Autowired
    Oauth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AppProperties appProperties,
            HttpCookieOauth2AuthRequestRepository httpCookieOauth2AuthRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
        this.httpCookieOauth2AuthRequestRepository = httpCookieOauth2AuthRequestRepository;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        Optional<String> redirectUri = CookieUtils
                .getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);
        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException(
                    "Unauthorized Redirect URI got. Can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String token = tokenProvider.createToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build()
                .toUriString();
    }


    protected void clearAuthenticationAttributes(HttpServletRequest request,
            HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOauth2AuthRequestRepository.removeAuthorizationRequestCookies(request, response);
    }


    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris().stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedUri = URI.create(authorizedRedirectUri);
                    return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedUri.getPort() == clientRedirectUri.getPort();
                });
    }

}
