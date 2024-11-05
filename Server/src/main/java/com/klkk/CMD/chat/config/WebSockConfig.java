package com.klkk.CMD.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독 prefix 설정
        registry.enableSimpleBroker("/sub");
        // 발행 prefix 설정
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // STOMP 엔드포인트 등록 및 CORS 설정
        registry.addEndpoint("/api/ws-stomp")
                .setAllowedOriginPatterns("https://comide.site")
                .withSockJS();  // SockJS 지원
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(10).maxPoolSize(100).queueCapacity(500);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(10).maxPoolSize(100).queueCapacity(500);
    }

    // ThreadPoolTaskExecutor 빈 설정
    @Bean
    public ThreadPoolTaskExecutor webSocketTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  // 기본 스레드 수
        executor.setMaxPoolSize(100);  // 최대 스레드 수
        executor.setQueueCapacity(500);  // 큐 용량
        executor.setThreadNamePrefix("WebSocket-");  // 스레드 이름
        executor.initialize();
        return executor;
    }
}