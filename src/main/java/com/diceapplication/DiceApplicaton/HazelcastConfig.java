package com.diceapplication.DiceApplicaton;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    private static final Logger logger = LoggerFactory.getLogger(HazelcastConfig.class);

    @Bean
    public HazelcastInstance hazelcastInstance() {
        logger.info("Configuring Hazelcast Client to connect to remote server...");
        
        ClientConfig clientConfig = new ClientConfig();
        
        // Set the client instance name
        clientConfig.setInstanceName("dice-roll-hazelcast-client");
        
        // Configure network connection to Hazelcast server
        // Server is running on localhost:5701
        clientConfig.getNetworkConfig().addAddress("localhost:5701");
        
        // Connection settings
        clientConfig.getNetworkConfig().setConnectionTimeout(10000); // 10 seconds
        clientConfig.getNetworkConfig().setConnectionAttemptLimit(5);
        clientConfig.getNetworkConfig().setConnectionAttemptPeriod(3000);
        
        // Enable smart routing for better performance
        clientConfig.getNetworkConfig().setSmartRouting(true);
        
        // Enable redelivery for reliable operations
        clientConfig.getNetworkConfig().setRedoOperation(true);
        
        logger.info("Hazelcast Client configured to connect to: localhost:5701");
        
        try {
            // Create and connect to Hazelcast server
            HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
            
            // Verify connection
            if (client != null) {
                logger.info("Successfully connected to Hazelcast Server!");
                logger.info("Client Name: {}", client.getName());
                logger.info("Cluster Members: {}", client.getCluster().getMembers().size());
            }
            
            return client;
        } catch (Exception e) {
            logger.error("Failed to connect to Hazelcast Server. Make sure the server is running on localhost:5701", e);
            throw new RuntimeException("Hazelcast client connection failed. Please ensure Hazelcast server is running.", e);
        }
    }
}

