package com.bootcamptoprod.bulkhead.service;

import com.bootcamptoprod.bulkhead.client.MovieApiClient;
import com.bootcamptoprod.bulkhead.entity.Movie;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
public class MovieService {

    private final Logger log = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private BulkheadRegistry bulkheadRegistry;

    @Autowired
    private MovieApiClient movieApiClient;


    @Bulkhead(name = "bulkheadWithConcurrentCalls")
    public Movie getMovieDetailsWithMaxConcurrentCallsBulkhead(String movieId) {
        return fetchMovieDetails(movieId);
    }

    @Bulkhead(name = "bulkheadWithConcurrentCallsAndWaitDuration")
    public Movie getMovieDetailsWithMaxConcurrentCallsAndMaxDurationBulkhead(String movieId) {
        return fetchMovieDetails(movieId);
    }

    @Bulkhead(name = "threadPoolBulkhead")
    public Movie getMovieDetailsWithThreadPoolBulkhead(String movieId) {
        return fetchMovieDetails(movieId);
    }

    @Bulkhead(name = "bulkheadWithConcurrentCalls", fallbackMethod = "fetchMovieDetailsFallbackMethod")
    public Movie getMovieDetailsWithMaxConcurrentCallsBulkheadAndFallback(String movieId) {
        return fetchMovieDetails(movieId);
    }

    @Bulkhead(name = "customBulkhead")
    public Movie getMovieDetailsWithCustomBulkhead(String movieId) {
        return fetchMovieDetails(movieId);
    }

    @Bulkhead(name = "customThreadPoolBulkhead")
    public Movie getMovieDetailsWithCustomThreadPoolBulkhead(String movieId) {
        return fetchMovieDetails(movieId);
    }

    public Movie fetchMovieDetailsFallbackMethod(String movieId, BulkheadFullException bulkheadFullException) {
        log.info("Fallback method called.");
        log.info("BulkheadFullException exception message: {}", bulkheadFullException.getMessage());
        return new Movie("Default", "N/A", "N/A", 0.0);
    }

    private Movie fetchMovieDetails(String movieId) {
        Movie movie = null;
        try {
            movie = movieApiClient.getMovieDetails(movieId);
        } catch (HttpServerErrorException httpServerErrorException) {
            log.error("Received HTTP server error exception while fetching the movie details. Error Message: {}", httpServerErrorException.getMessage());
            throw httpServerErrorException;
        } catch (HttpClientErrorException httpClientErrorException) {
            log.error("Received HTTP client error exception while fetching the movie details. Error Message: {}", httpClientErrorException.getMessage());
            throw httpClientErrorException;
        } catch (ResourceAccessException resourceAccessException) {
            log.error("Received Resource Access exception while fetching the movie details.");
            throw resourceAccessException;
        } catch (Exception exception) {
            log.error("Unexpected error encountered while fetching the movie details" + exception);
            throw exception;
        }
        return movie;
    }

    @PostConstruct
    public void postConstruct() {
        var eventPublisher = bulkheadRegistry.bulkhead("bulkheadWithConcurrentCalls").getEventPublisher();
        eventPublisher.onEvent(event -> System.out.println("Bulkhead with concurrent calls - On Event. Event Details: " + event));
        eventPublisher.onCallPermitted(event -> System.out.println("Bulkhead with concurrent calls  - On call permitted. Event Details: " + event));
        eventPublisher.onCallRejected(event -> System.out.println("Bulkhead with concurrent calls  - On call rejected. Event Details: " + event));
        eventPublisher.onCallFinished(event -> System.out.println("Bulkhead with concurrent calls  - On call finished. Event Details: " + event));


        bulkheadRegistry.getEventPublisher().onEntryAdded(entryAddedEvent -> {
                    var addedBulkhead = entryAddedEvent.getAddedEntry();
                    System.out.println("Bulkhead added: " + addedBulkhead.getName());
                })
                .onEntryRemoved(entryRemovedEvent -> {
                    var removedBulkhead = entryRemovedEvent.getRemovedEntry();
                    System.out.println("Bulkhead removed: " + removedBulkhead.getName());
                })
                .onEntryReplaced(entryReplacedEvent -> {
                    var oldBulkhead = entryReplacedEvent.getOldEntry();
                    var newBulkhead = entryReplacedEvent.getNewEntry();
                    System.out.println("Bulkhead " + oldBulkhead + " replaced with " + newBulkhead);
                });
    }

}
