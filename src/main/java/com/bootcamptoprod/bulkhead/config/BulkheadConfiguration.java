package com.bootcamptoprod.bulkhead.config;

import io.github.resilience4j.bulkhead.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BulkheadConfiguration {

    @Autowired
    private BulkheadRegistry bulkheadRegistry;

    @Autowired
    private ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry;

    @Bean
    public Bulkhead bulkheadWithCustomConfig() {
        BulkheadConfig customConfig = BulkheadConfig.custom()
                .maxConcurrentCalls(5)
                .maxWaitDuration(Duration.ofMillis(200))
                .writableStackTraceEnabled(true)
                .build();

        return bulkheadRegistry.bulkhead("customBulkhead", customConfig);
    }

    @Bean
    public ThreadPoolBulkhead threadPoolBulkheadWithCustomConfig() {
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
                .maxThreadPoolSize(1)
                .coreThreadPoolSize(1)
                .queueCapacity(1)
                .writableStackTraceEnabled(true)
                .build();

        return threadPoolBulkheadRegistry.bulkhead("customThreadPoolBulkhead", config);
    }
}
