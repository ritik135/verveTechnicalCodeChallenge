package com.example.verve.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.verve.config.KafkaProducerConfig;
import com.example.verve.service.RequestTrackerService;

@Component
@EnableScheduling
public class LoggingScheduler {

    private static final Logger logger = LoggerFactory.getLogger(LoggingScheduler.class);

    @Autowired
    private RequestTrackerService requestTrackerService;
    
    @Autowired
    private KafkaProducerConfig kafkaProducer;
    
    @Value("${kafka.send.unique.count.topic}")
    private String kafkaTopicName;
    
    
    // Scheduled task to log the unique request count every minute using cron expression
    @Scheduled(cron = "${cron.job.unique.request.count}") // Cron expression to run every minute
    public void logUniqueRequestCount() {
    	logger.info("Cron Job Started");
    	try {
            // Get unique request count from the service
            int count = requestTrackerService.getUniqueRequestCount();  
            logger.info("Unique request count in the last minute: {}", count);

            // Send the count to Kafka for streaming
            kafkaProducer.sendMessage(kafkaTopicName, "Unique request count in the last minute: " + count);
        } catch (Exception e) {
            // Log any exceptions that occur during the execution of the scheduled task
            logger.error("Error during logging unique request count: {}", e.getMessage(), e);
        } finally {
        	// Reset the request count for the next minute
            requestTrackerService.resetRequestCount();
        }
    	logger.info("Cron Job Finished");
    }
}

