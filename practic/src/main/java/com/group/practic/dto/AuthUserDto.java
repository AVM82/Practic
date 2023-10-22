package com.group.practic.dto;

import com.group.practic.entity.PersonEntity;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder
public class AuthUserDto implements OAuth2User, OidcUser {

    @Serial
    private static final long serialVersionUID = -2845160792248762779L;
    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;
    private Map<String, Object> attributes;

    @Getter
    private PersonEntity person;
    private String email;
    private String password;
    private boolean isEnabled;
    private boolean isAccountNotExpired;
    private boolean isCredentialsNotExpired;
    private boolean isAccountNotLocked;
    private Collection<? extends GrantedAuthority> authorities;

    public static AuthUserDto create(
            PersonEntity person, Map<String, Object> attributes,
            OidcIdToken idToken, OidcUserInfo userInfo
    ) {
        return AuthUserDto.builder()
                .email(person.getEmail())
                .password(person.getPassword())
                .isEnabled(person.isEnabled())
                .isAccountNotExpired(person.isAccountNonExpired())
                .isCredentialsNotExpired(person.isCredentialsNonExpired())
                .isAccountNotLocked(person.isAccountNonLocked())
                .authorities(person.getAuthorities())
                .person(person)
                .idToken(idToken)
                .userInfo(userInfo)
                .attributes(attributes)
                .build();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }

    @Override
    public String getName() {
        return this.person.getName();
    }

    public Long getId() {
        return this.person.getId();
    }
}
