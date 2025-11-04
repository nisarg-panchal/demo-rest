package com.nisarg.spring.demorest.config;

import com.nisarg.spring.demorest.helper.CustomRedissonRegionFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedissonCacheConfig {

  private final RedissonClient redissonClient;

  @PostConstruct
  public void init() {
    log.info("init RedissonCacheConfig");
    new CustomRedissonRegionFactory().setRedissonClient(redissonClient);
  }
}
