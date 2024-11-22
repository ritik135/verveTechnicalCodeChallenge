package com.example.verve.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.verve.service.HttpRequestService;
import com.example.verve.service.RequestTrackerService;
@RestController
@RequestMapping("/api/verve")
public class VerveController {
	
	private static final Logger logger = LoggerFactory.getLogger(VerveController.class);
	
	private final RequestTrackerService requestService;
    private final HttpRequestService httpRequestService;

    // Constructor injection
    @Autowired
    public VerveController(RequestTrackerService requestService, HttpRequestService httpRequestService) {
        this.requestService = requestService;
        this.httpRequestService = httpRequestService;
    }

	/**
	 * This endpoint handles incoming HTTP requests to track a unique request based on the provided ID.
	 * It checks if the request is unique, logs the result, and optionally fires an HTTP request to an external endpoint
	 * with the current unique request count. The HTTP method used for the external request can be either GET or POST,
	 * with GET being the default.
	 * 
	 * The method performs the following:
	 * - Logs the receipt of the request with the provided ID.
	 * - Checks if the request ID is unique using {@link RequestService#trackRequest(int)}.
	 * - If the request is unique, it logs the unique request and optionally sends an HTTP request to the specified
	 *   endpoint using the {@link HttpRequestService#fireHttpRequest(String, int, String)} method.
	 * - If the request is not unique, it logs the duplicate request.
	 * 
	 * @param id The unique identifier of the incoming request.
	 * @param endpoint The endpoint to which the HTTP request will be sent (optional).
	 * @param method The HTTP method to be used for the external request (GET/POST), default is GET.
	 * @return A {@link ResponseEntity} with status "ok" if the request was processed successfully, or "failed"
	 *         with a 500 status code if an error occurs.
	 */
	@GetMapping("/accept")
    public ResponseEntity<String> acceptRequest(@RequestParam("id") int id, 
    											@RequestParam(value = "endpoint", required = false) String endpoint,
    											@RequestParam(value = "method", required = false, defaultValue = "GET") String method) {
		logger.info("Received request with id: {}", id);
        try {
            boolean isUnique = requestService.trackRequest(id);
            if (isUnique) {
                logger.info("Request with id: {} is unique.", id);
            } else {
                logger.info("Request with id: {} is duplicate.", id);
            }  
            if (endpoint != null) {
                int uniqueCount = requestService.getUniqueRequestCount();
                httpRequestService.fireHttpRequest(endpoint, uniqueCount, method);
            }
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("failed");
        }
	}

}
