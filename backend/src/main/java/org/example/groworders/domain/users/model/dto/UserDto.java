package org.example.groworders.domain.users.model.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.groworders.domain.users.model.entity.Role;
import org.example.groworders.domain.users.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UserDto {
    @Getter
    public static class SignIn {
        private String email;
        private String password;
    }

    @Getter
    @Builder
    public static class SignInResponse {
        private Long id;
        private String email;
        private String name;
        private Role role;

        public static SignInResponse from(AuthUser authUser) {

            return SignInResponse.builder()
                    .id(authUser.getId())
                    .email(authUser.getEmail())
                    .name(authUser.getName())
                    .role(authUser.getRole())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class UserResponse {
        private Long id;
        private String email;
        private String name;
        private Role role;

        public static UserResponse from(User entity) {

            return UserResponse.builder()
                    .id(entity.getId())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .role(entity.getRole())
                    .build();
        }
    }

    @Getter
    public static class SignUp {
        private String accountId;
        private String email;
        private String password;
        private String name;
        private String phoneNumber;
        private LocalDate birthDate;
        private Role role;

        public User toEntity() {
            return User.builder()
                    .accountId(accountId)
                    .email(email)
                    .password(password)
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .birthDate(birthDate)
                    .role(role != null ? role : Role.USER)
                    .enabled(false)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class AuthUser implements UserDetails, OAuth2User {
        private Long id;
        private String email;
        private String password;
        private String name;
        private Role role;
        private Boolean enabled;
        private Map<String, Object> attributes;

        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }


        public static AuthUser from(User entity) {
            return AuthUser.builder()
                    .id(entity.getId())
                    .email(entity.getEmail())
                    .password(entity.getPassword())
                    .name(entity.getName())
                    .role(entity.getRole() != null ? entity.getRole() : Role.USER)
                    .enabled(entity.getEnabled())
                    .build();
        }

        public User toEntity() {
            return User.builder()
                    .id(id)
                    .build();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
        }

        @Override
        public String getUsername() {
            return email;
        }

        public String getPassword() {
            return "{noop}" + password;
        }
    }

}
