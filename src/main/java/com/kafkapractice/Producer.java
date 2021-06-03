package com.kafkapractice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import static com.kafkapractice.Config.*;

@Data
@Slf4j
public class Producer {

    public static void main(String[] args) throws InterruptedException {
        final MockClient client = mockClient();
        final KafkaProducer<String, String> producer = createKafkaProducer();
        final Callback callback = (recordMetadata, exception) -> {
            if (exception == null) {
                log.info(fillMetadataLogInfo(recordMetadata));
            } else {
                log.error("Error while producing!", exception);
            }
        };

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("shutting down the app..");
            producer.flush();
            producer.close();
            log.info("shut down successful..");
        }));

        for(int i=0; i<10000; i++) {
            final String event = client.createEvent();

            final ProducerRecord<String, String> record = new ProducerRecord<>(KAFKA_TOPIC, null , event);

            producer.send(record, callback);

            final int sleep = ThreadLocalRandom.current().nextInt(10, 300);

            Thread.sleep(sleep);
        }
    }

    public static MockClient mockClient() {

        return new MockClient();
    }

    public static KafkaProducer<String, String> createKafkaProducer() {
        final Properties properties = setProperties();

        return new KafkaProducer<>(properties);
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
