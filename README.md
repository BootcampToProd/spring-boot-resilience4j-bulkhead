

## Resilience4j Bulkhead: Building Robust Services with Concurrency Limits
For complete understanding of Resilience4j Bulkhead and how we can integrate it inside the Spring Boot application you can checkout our blog.
<br/><br/>**Blog Link:** [Resilience4j Bulkhead: Building Robust Services with Concurrency Limits](https://bootcamptoprod.com/spring-boot-resilience4j-bulkhead/)
<br/>

# spring-boot-resilience4j-bulkhead
A simple app demonstrating how we can implement bulkhead pattern using Resilience4j in Spring Boot

## App Overview
This is a simple app wherein we are fetching the movie details based on the movie id. The movie details are fetched from external service that is called using the Spring Rest Template. For simplicity, we have created a mock controller which acts as an external service for returning the movie details.

## Bulkhead Scenarios
We have created a single controller endpoint which accepts movie id as path parameter and query parameter bulkheadType which accepts predefined set of values to mimic the different bulkhead examples.

### Acceptable Values

#### For Path Parameter - Movie Id
a. **1** - Mock controller returns valid movie information<br/>
b. **2** - Mock controller returns valid movie information but with a delay<br/>
c. **3** - Mock controller returns HTTP status code 404<br/>
d. **4** or **any other numeric value** - Mock controller returns null which leads to MovieNotFound Exception

#### For Query Parameter - bulkheadType
Different bulkhead instances are defined inside the application.yml. To mimic different scenarios use:<br/>
a. **bulkhead-with-max-concurrent-calls:** bulkheadWithConcurrentCalls bulkhead instance will be triggered<br/>
b. **bulkhead-with-max-concurrent-calls-and-max-wait-duration:** bulkheadWithConcurrentCallsAndWaitDuration bulkhead instance will be triggered.<br/>
c. **thread-pool-bulkhead:** threadPoolBulkhead bulkhead instance will be triggered.<br/>
d. **bulkhead-with-max-concurrent-calls-and-fallback:** bulkheadWithConcurrentCalls bulkhead instance will be triggered and fallback method logic will be executed in this case.<br/>
e. **custom-bulkhead:** customBulkhead bulkhead instance will be triggered.<br/>
f. **custom-thread-pool-bulkhead:** customThreadPoolBulkhead bulkhead instance will be triggered.<br/>

## JMeter Test Plan
In order to test different scenarios, run the respective Thread Group inside JMeter. Check the application logs in order to get the better understanding of different bulkhead scenarios.

The JMeter Test Plan is available under the resources folder.
<br/>[src > main > resources > jmeter > Resilience4j-bulkhead.jmx]

## Testing Bulkhead Examples
a. **Import JMeter Test Plan:** Begin by importing the JMeter test plan provided in the resources folder into your JMeter application.<br/>
b. **Enable Respective Thread Group:** Based on the scenario you wish to test (e.g., bulkhead-with-max-concurrent-calls or thread-pool-bulkhead), ensure the corresponding thread group is enabled in the test plan. Disable other thread groups to prevent conflicting test scenarios.<br/>
c. **Review Thread Group Settings:** Verify the number of threads or users configured within the enabled thread group. This information can be found in the respective thread group settings.<br/>
d. **Inspect HTTP Request Configuration:** Within the “HTTP Request” sampler of the enabled thread group, review the configured host, port, and endpoint of the application. Confirm that the bulkheadType query parameter is set correctly to define the scenario to test.<br/>
e. **View Output:** After executing the test, examine the output and results in the “View Results Tree” listener of the respective thread group. This listener provides detailed insights into the requests sent, received responses, and any encountered errors or issues during testing.<br/>

### 1. Bulkhead with Max Concurrent Calls
```
Run "bulkhead-with-max-concurrent-calls" JMeter thread group
```

### 2. Bulkhead with Max Concurrent Calls and Max Wait Duration
```
Run "bulkhead-with-max-concurrent-calls-and-max-wait-duration-calls" JMeter thread group
```

### 3. Thread Pool Bulkhead
```
Run "thread-pool-bulkhead" JMeter thread group
```

### 4. Bulkhead with Max Concurrent Calls and Fallback Method
```
Run "bulkhead-with-max-concurrent-calls-and-fallback" JMeter thread group
```

### 5. Custom Bulkhead
```
Run "custom-bulkhead" JMeter thread group
```

### 6. Custom Thread Pool Bulkhead
```
Run "custom-thread-pool-bulkhead" JMeter thread group
```

## Postman Collection
Additionally, the Postman collection is available under the resources folder containing all the requests including actuator endpoints.
<br/>[src > main > resources > postman > Spring-Boot-Resilience4j-Bulkhead.postman_collection.json]