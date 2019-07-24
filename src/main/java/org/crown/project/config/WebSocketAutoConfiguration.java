package org.crown.project.config;

import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;

import org.crown.framework.manager.ThreadExecutors;
import org.crown.framework.pushlog.LogMessage;
import org.crown.framework.pushlog.LoggerQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.extern.slf4j.Slf4j;

/**
 * 配置WebSocket消息代理端点，即stomp服务端
 *
 * @author Caratacus
 * @link https://cloud.tencent.com/developer/article/1096792
 */
@Slf4j
@Configuration
public class WebSocketAutoConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .withSockJS();
    }

    /**
     * 推送日志到/topic/pullLogger
     */
    @PostConstruct
    public void pushLogger() {
        ScheduledExecutorService executorService = ThreadExecutors.getExecutorService();
        Runnable runnable = () -> {
            for (; ; ) {
                try {
                    LogMessage log = LoggerQueue.getInstance().poll();
                    if (log != null && messagingTemplate != null) {
                        messagingTemplate.convertAndSend("/topic/pullLogger", log);
                    }
                } catch (Exception e) {
                    log.warn("推送日志失败:{}", e.getMessage());
                }
            }
        };
        executorService.submit(runnable);
    }
}