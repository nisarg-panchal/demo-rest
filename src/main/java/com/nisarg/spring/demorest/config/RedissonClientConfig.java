package com.nisarg.spring.demorest.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RedissonClientConfig {

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redissonClient() {
    log.info("init redissonClient");
    Config config = new Config();
    config.useSingleServer()
        .setAddress("redis://127.0.0.1:6379"); // or from properties
    return Redisson.create(config);
  }
}
