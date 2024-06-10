[//]: # (ADDIND LOGS TO THE APPLICATION)

Adding SLF4J logging to a Spring application is straightforward. SLF4J (Simple Logging Facade for Java) is a logging framework that serves as an abstraction for various logging frameworks (e.g., Logback, Log4j). Below are the steps to integrate SLF4J logging into your Spring application.

### 1. Add Dependencies

First, ensure you have the necessary dependencies in your `pom.xml` (for Maven) or `build.gradle` (for Gradle).

#### Maven

Add the SLF4J and Logback dependencies to your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-logging</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

The `spring-boot-starter-logging` starter includes SLF4J and Logback by default.

#### Gradle

Add the dependencies to your `build.gradle`:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-logging'
implementation 'org.springframework.boot:spring-boot-starter-web'
```

### 2. Create a Logger

Create a logger in your Spring components (e.g., controllers, services) using SLF4J. Here’s an example in a controller:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> registerUser(@RequestBody SignupRequest signUpRequest) {
        logger.info("Received signup request for user: {}", signUpRequest.getUsername());
        // Your signup logic here
        return Mono.just(ResponseEntity.ok("User registered successfully"));
    }
}
```

### 3. Configure Logging Level

You can configure the logging levels (e.g., DEBUG, INFO, WARN, ERROR) in your `application.properties` or `application.yml` file.

#### application.properties

```properties
logging.level.root=INFO
logging.level.org.springframework.web=DEBUG
logging.level.com.yourpackage=DEBUG
```

#### application.yml

```yaml
logging:
  level:
    root: INFO
    org.springframework.web: DEBUG
    com.yourpackage: DEBUG
```

### 4. Customize Logback Configuration (Optional)

If you need more advanced logging configurations, you can create a `logback-spring.xml` file in your `src/main/resources` directory. Here’s an example configuration:

```xml
<configuration>

    <property name="LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n"/>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <logger name="org.springframework.web" level="DEBUG"/>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>

</configuration>
```

### 5. Testing Logging

Run your Spring Boot application and test the logging by hitting the endpoints and checking the logs printed in the console.

### Example

Here's a complete example with SLF4J logging integrated into a simple Spring Boot application:

#### Main Application

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

#### AuthController

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> registerUser(@RequestBody SignupRequest signUpRequest) {
        logger.info("Received signup request for user: {}", signUpRequest.getUsername());
        // Your signup logic here
        return Mono.just(ResponseEntity.ok("User registered successfully"));
    }
}
```

#### application.properties

```properties
logging.level.root=INFO
logging.level.org.springframework.web=DEBUG
logging.level.com.yourpackage=DEBUG
```

By following these steps, you can integrate SLF4J logging into your Spring application, allowing you to log messages at various levels and control the logging output.