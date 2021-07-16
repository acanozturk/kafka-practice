package com.kafkaproducer.services;

import lombok.Data;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

import static com.kafkaproducer.configs.ProducerConfig.BOOTSTRAP_SERVER;
import static com.kafkaproducer.configs.ProducerConfig.SERIALIZER;

@Data
public class ProducerPropertyService {

    public static Properties setProperties() {
        final Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, SERIALIZER);
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SERIALIZER);
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");

        return properties;
    }

}
