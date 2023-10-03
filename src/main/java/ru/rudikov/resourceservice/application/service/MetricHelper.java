package ru.rudikov.resourceservice.application.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class MetricHelper {

  private final MeterRegistry registry;

  private final AtomicInteger userGauge;

  private static final String USER_GAUGE_NAME = "user_gauge";
  private static final String LOGIN_COUNTER_NAME = "login_counter";
  private static final String RESULT_TAG_NAME = "result";

  public static final String SUCCESS_RESULT = "success";
  public static final String FAILED_RESULT = "failed";

  public MetricHelper(MeterRegistry registry, MeterRegistry meterRegistry) {
    this.registry = registry;
    userGauge = meterRegistry.gauge(USER_GAUGE_NAME, new AtomicInteger(0));
  }

  public Counter loginCounter(String result) {
    return Counter.builder(LOGIN_COUNTER_NAME).tag(RESULT_TAG_NAME, result).register(registry);
  }

  public void updateUserGauge(Long count) {
    userGauge.set(count.intValue());
  }
}
