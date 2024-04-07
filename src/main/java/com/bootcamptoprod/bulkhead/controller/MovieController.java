package com.bootcamptoprod.bulkhead.controller;

import com.bootcamptoprod.bulkhead.entity.Movie;
import com.bootcamptoprod.bulkhead.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/movies")
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String id, @RequestParam(defaultValue = "bulkhead-with-max-concurrent-calls") String bulkheadType) {
        switch (bulkheadType) {
            case "bulkhead-with-max-concurrent-calls" -> {
                log.info("Bulkhead with max concurrent calls example");
                Movie movie = movieService.getMovieDetailsWithMaxConcurrentCallsBulkhead(id);
                log.info("Response: {}", movie);
                return ResponseEntity.ok(movie);
            }
            case "bulkhead-with-max-concurrent-calls-and-max-wait-duration" -> {
                log.info("Bulkhead with max concurrent calls and max wait duration example");
                Movie movie = movieService.getMovieDetailsWithMaxConcurrentCallsAndMaxDurationBulkhead(id);
                log.info("Response: {}", movie);
                return ResponseEntity.ok(movie);
            }
            case "bulkhead-with-max-concurrent-calls-and-fallback" -> {
                log.info("Bulkhead with max concurrent calls and fallback method example");
                Movie movie = movieService.getMovieDetailsWithMaxConcurrentCallsBulkheadAndFallback(id);
                log.info("Response: {}", movie);
                return ResponseEntity.ok(movie);
            }
            case "thread-pool-bulkhead" -> {
                log.info("Thread pool bulkhead example");
                Movie movie = movieService.getMovieDetailsWithThreadPoolBulkhead(id);
                log.info("Response: {}", movie);
                return ResponseEntity.ok(movie);
            }
            case "custom-bulkhead" -> {
                log.info("Custom bulkhead with max concurrent calls and max wait duration example");
                Movie movie = movieService.getMovieDetailsWithCustomBulkhead(id);
                log.info("Response: {}", movie);
                return ResponseEntity.ok(movie);
            }
            case "custom-thread-pool-bulkhead" -> {
                log.info("Custom thread pool bulkhead example");
                Movie movie = movieService.getMovieDetailsWithCustomThreadPoolBulkhead(id);
                log.info("Response: {}", movie);
                return ResponseEntity.ok(movie);
            }
        }
        return null;
    }
}
