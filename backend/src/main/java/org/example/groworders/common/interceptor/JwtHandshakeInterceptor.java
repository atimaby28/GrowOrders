package org.example.groworders.common.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.groworders.domain.users.model.dto.UserDto;
import org.example.groworders.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpReq = servletRequest.getServletRequest();

            String jwt = extractJwt(httpReq);
            if (jwt == null || jwt.isBlank()) {
                // JWT 없으면 그냥 연결 허용 (SockJS fallback용)
                return true;
            }

            Claims claims = JwtUtil.getClaims(jwt);
            if (claims == null || JwtUtil.isExpired(claims)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            try {
                UserDto.AuthUser authUser = UserDto.AuthUser.builder()
                        .id(Long.parseLong(JwtUtil.getValue(claims, "id")))
                        .email(JwtUtil.getValue(claims, "email"))
                        .build();

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        authUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

                attributes.put("auth", authentication);
            } catch (Exception e) {
                // parsing 오류 등 발생 시 연결 허용
                System.out.println("WebSocket JWT parsing error: " + e.getMessage());
            }
        }
        return true;
    }

    private String extractJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("OL_AT".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
