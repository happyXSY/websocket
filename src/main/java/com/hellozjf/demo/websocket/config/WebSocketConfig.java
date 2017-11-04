package com.hellozjf.demo.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

// 表明它是一个Spring配置类
@Configuration
// 启用WebSocket消息处理，由消息代理支持
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 从客户端过来的消息都是以/app开头
        config.setApplicationDestinationPrefixes("/app");
        // 发回给客户端的消息都是以/topic开头
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 启用SockJS后备选项，以便在WebSocket不可用时可以使用替代传输
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }

}