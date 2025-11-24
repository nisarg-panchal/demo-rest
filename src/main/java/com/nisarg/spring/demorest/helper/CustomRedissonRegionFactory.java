package com.nisarg.spring.demorest.helper;

import java.util.Map;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.CacheException;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.hibernate.RedissonRegionFactory;

@Slf4j
public class CustomRedissonRegionFactory extends RedissonRegionFactory {

  @Setter
  private RedissonClient redissonClient;

  public CustomRedissonRegionFactory() {
    super();
  }

  @Override
  protected RedissonClient createRedissonClient(Map properties) {
    if (this.redissonClient == null) {
      try {
        // Read Redis address from properties or use default
        String redisAddress = "redis://127.0.0.1:6379";
        if (properties != null && properties.containsKey("hibernate.cache.redisson.address")) {
          redisAddress = (String) properties.get("hibernate.cache.redisson.address");
        }

        Config config = new Config();
        config.useSingleServer().setAddress(redisAddress);

        this.redissonClient = Redisson.create(config);
      } catch (Exception e) {
        throw new CacheException("Failed to create RedissonClient", e);
      }
    }
    return this.redissonClient;
  }
}
