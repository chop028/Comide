package com.klkk.CMD.security.dto;

import com.klkk.CMD.security.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
public class CustomOAuth2User implements OAuth2User {
    private Long id;
    private String googleId;
    private String name;
    private String email;
    private String picture;
    private String comment;  // 상태 메시지
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    // User 엔티티로부터 생성
    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.id = user.getId();  // User 엔티티의 모든 필드를 포함
        this.googleId = user.getGoogleId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.comment = user.getComment();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority()));
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}