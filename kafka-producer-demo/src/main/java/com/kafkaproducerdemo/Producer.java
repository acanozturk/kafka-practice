package com.kafkaproducerdemo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import static com.kafkaproducerdemo.Config.*;

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
        }));

        for(int i=0; i<10000; i++) {
            final String event = client.createEvent();

            final ProducerRecord<String, String> record = new ProducerRecord<>(KAFKA_TOPIC, null , event);

            producer.send(record, callback);

            final int sleep = ThreadLocalRandom.current().nextInt(10, 150);

            Thread.sleep(sleep);
        }
    }

    public static MockClient mockClient() {

        return new MockClient();
    }

    public static KafkaProducer<String, String> createKafkaProducer() {
        final Properties producerProperties = setProperties();

        return new KafkaProducer<>(producerProperties);
    }

    private static Properties setProperties() {
        final Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, SERIALIZER);
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SERIALIZER);
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024));

        return properties;
    }

    private static String fillMetadataLogInfo(final RecordMetadata recordMetadata) {

        return "\nReceived new metadata. \n" +
                "Topic: " + recordMetadata.topic() + "\n" +
                "Partition: " + recordMetadata.partition() + "\n" +
                "Offset: " + recordMetadata.offset() + "\n";
    }

}
