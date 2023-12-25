package com.group.practic.security.user;

import java.util.Map;

public abstract class Oauth2UserInfo {

    protected Map<String, Object> attributes;

    
    protected Oauth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    public Map<String, Object> getAttributes() {
        return attributes;
    }


    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

}
