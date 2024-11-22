package com.example.verve.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RequestTrackerService {

    private static final Logger logger = LoggerFactory.getLogger(RequestTrackerService.class);

    private AtomicInteger requestCount = new AtomicInteger(0);
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    /**
     * Tracks unique requests by checking the presence of the request ID in Redis.
     * If the ID is not present, it is considered a new request and the unique request 
     * count is incremented and an entry is created in Redis Cache.
     *
     * @param id the unique identifier for the request.
     * @return true if the request is unique (i.e., it was added to Redis); 
     *         false if the request is a duplicate (i.e., it already exists in Redis).
     */
    public boolean trackRequest(int id) {
        String uniqueKey = "uniqueId:" + id;
        logger.info("Attempting to track request with ID: " + id);
        
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(uniqueKey, "1", Duration.ofMinutes(1));

        if (isNew != null && isNew) {
            requestCount.incrementAndGet();
        }
        return isNew != null && isNew;
    }

    
    /**
     * @return the current count of unique requests
     */
    public int getUniqueRequestCount() {
        return requestCount.get();
    }

    
    /**
     * @return the request count for the next minute
     */
    public void resetRequestCount() {
        requestCount.set(0);
    }
}
