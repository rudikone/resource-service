package ru.rudikov.resourceservice.adapter.secondary.kafka;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.rudikov.resourceservice.application.port.secondary.AuthLoggingPort;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthLoggingProducer implements AuthLoggingPort {

  @Value("${kafka.producer.user-auth-producer.topic}")
  private String topicName;

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public void send(String login, String operation) {
    sendMessageWithCallback(login, operation);
  }

  private void sendMessageWithCallback(String key, String message) {
    CompletableFuture<SendResult<String, String>> future =
        kafkaTemplate.send(topicName, key, message);

    future.whenComplete(
        (result, throwable) -> {
          if (throwable != null) {
            log.warn("Unable to deliver message [{}]. {}", message, throwable.getMessage());
          } else {
            log.info(
                "Message [{}] delivered with offset {}",
                message,
                result.getRecordMetadata().offset());
          }
        });
  }
}
