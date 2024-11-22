# Thought Process - High-Level Overview

## Problem Overview
The goal is to create a service that tracks unique requests, logs the count every minute, and optionally sends that data to an external endpoint. The service should be scalable and resilient to handle high volumes of requests.

## Design Considerations
- **Redis** for caching and unique request tracking
- **Scheduled Cron Job tasks** to log counts and send data to Kafka
- **HttpRequestService** to handle both GET and POST requests to external endpoints
- **Scalable architecture** using Spring Boot, Redis, and Kafka for scalability

## Key Components
- **VerveController**: Exposes the API endpoint.
- **RequestTrackerService**: Handles request tracking and logic.
- **HttpRequestService**: Sends GET/POST requests to external endpoints.
- **LoggingScheduler**: Logs request counts every minute.

## Conclusion
This approach ensures a scalable, fault-tolerant system while keeping the application modular and easy to extend.