package com.kafkapractice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Data
@Slf4j
public class Producer {

    public static void main(String[] args) {
        final Properties properties = setProperties();

        final KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        final ProducerRecord<String, String> record = new ProducerRecord<>("first_topic", "test_message");

        producer.send(record);
        producer.flush();
        producer.close();
    }

    private static Properties setProperties() {
        final String BOOTSTRAP_SERVER = "127.0.0.1:9092";
        final String SERIALIZER = StringSerializer.class.getName();

        final Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, SERIALIZER);
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SERIALIZER);

        return properties;
    }

}
