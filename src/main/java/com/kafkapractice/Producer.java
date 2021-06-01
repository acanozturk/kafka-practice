package com.kafkapractice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Data
@Slf4j
public class Producer {

    private final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    private final static String SERIALIZER = StringSerializer.class.getName();

    public static void main(String[] args) {
        final Properties properties = setProperties();

        final KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        final Callback callback = (recordMetadata, exception) -> {
            if(exception == null) {
                log.info(fillMetadataLogInfo(recordMetadata));
            } else {
                log.error("Error while producing!", exception);
            }
        };

        for(int i=0; i<10; i++) {
            String topic = "first_topic";
            String key = "key_" + i;
            String value = "value_" + i;

            final ProducerRecord<String, String> record = new ProducerRecord<>(topic, key ,value);

            log.info("Key: " + key);

            producer.send(record, callback);
        }

        producer.flush();
        producer.close();
    }

    private static Properties setProperties() {
        final Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, SERIALIZER);
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SERIALIZER);

        return properties;
    }

    private static String fillMetadataLogInfo(final RecordMetadata recordMetadata) {

        return "\nReceived new metadata. \n" +
               "Topic: " + recordMetadata.topic() + "\n" +
               "Partition: " + recordMetadata.partition() + "\n" +
               "Offset: " + recordMetadata.offset() + "\n";
    }
}
