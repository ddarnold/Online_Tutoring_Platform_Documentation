package de.thu.thutorium.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * WebSocket configuration class for setting up WebSocket communication in the application. This
 * configuration enables WebSocket message brokers and defines endpoints for client-to-server and
 * server-to-client communication.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig
    implements org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer {
  /**
   * Configures the message broker for handling WebSocket communication. This method enables a
   * simple message broker for broadcasting messages to subscribed clients and sets the prefix for
   * application-specific routes.
   *
   * @param registry the message broker registry for configuring the message broker
   */
  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // Enable a simple broker for handling message delivery to "/topic" destinations
    registry.enableSimpleBroker("/topic"); // For broadcasting messages
    // Set the prefix for application-level routes (client-to-server messages)
    registry.setApplicationDestinationPrefixes("/app"); // Prefix for client-to-server communication
  }

  /**
   * Registers the STOMP endpoint for WebSocket communication. This method configures the endpoint
   * that clients will use to connect to the server for real-time communication over WebSockets. It
   * also sets allowed origins for CORS and adds SockJS fallback options for browsers that do not
   * support WebSockets.
   *
   * @param registry the registry to configure WebSocket endpoint
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // Register "/chat" endpoint for WebSocket connections and enable SockJS fallback
    registry
        .addEndpoint("/chat")
        .setAllowedOrigins("http://localhost:3000")
        .withSockJS(); // WebSocket endpoint with fallback
  }
}
