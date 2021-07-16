package com.kafkastreams.services;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

import static com.kafkastreams.configs.StreamsConfig.*;

@Data
public class BankBalancePropertyService {

    public static Properties setProperties() {
        final Properties properties = new Properties();

        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
        properties.setProperty(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return properties;
    }
}
