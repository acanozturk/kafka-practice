package com.kafkapractice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

import static com.kafkapractice.Config.*;

@Data
@Slf4j
public class Producer {

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
            String key = "key_" + i;
            String value = "value_" + i;

            final ProducerRecord<String, String> record = new ProducerRecord<>(KAFKA_TOPIC, key ,value);

            log.info("Key: " + key);

            producer.send(record, callback);
        }

        producer.flush();
        producer.close();
    }

    private static Properties setProperties() {
        final Properties newProperties = new Properties();

        newProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        newProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, SERIALIZER);
        newProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SERIALIZER);

        return newProperties;
    }

    private static String fillMetadataLogInfo(final RecordMetadata recordMetadata) {

        return "\nReceived new metadata. \n" +
               "Topic: " + recordMetadata.topic() + "\n" +
               "Partition: " + recordMetadata.partition() + "\n" +
               "Offset: " + recordMetadata.offset() + "\n";
    }
}
