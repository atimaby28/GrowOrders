package org.example.groworders.common.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            // CONNECT 시 세션에서 auth 가져오기
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                Authentication authentication = null;
                if (accessor.getSessionAttributes() != null) {
                    Object authObj = accessor.getSessionAttributes().get("auth");
                    if (authObj instanceof Authentication) {
                        authentication = (Authentication) authObj;
                    }
                }
                if (authentication != null) {
                    accessor.setUser(authentication);
                }
            }

            // SEND, SUBSCRIBE 시 인증 없으면 경고 로그
            if ((StompCommand.SEND.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand()))
                    && accessor.getUser() == null) {
                System.out.println("WARN: 인증 없는 사용자가 메시지를 보내려고 함.");
            }
        }
        return message;
    }
}
