package com.bustation.mongodb.integration_tests;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public abstract class BaseIT {

    @Container
    protected static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:6.0")
        .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
    	mongoContainer.start();
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
    }
}