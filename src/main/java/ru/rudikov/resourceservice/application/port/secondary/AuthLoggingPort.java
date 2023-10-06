package ru.rudikov.resourceservice.application.port.secondary;

public interface AuthLoggingPort {

  void send(String login, String operation);
}
