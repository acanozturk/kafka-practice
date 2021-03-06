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
        final MockClient client = createMockClient();
        final KafkaProducer<String, String> producer = createKafkaProducer();

        startEventProducing(client, producer);

        shutdownHook(producer);
    }

    public static MockClient createMockClient() {

        return new MockClient();
    }

    public static KafkaProducer<String, String> createKafkaProducer() {

        return new KafkaProducer<>(setProperties());
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

    private static void startEventProducing(final MockClient client, final KafkaProducer<String, String> producer)
            throws InterruptedException {

        for(int i=0; i<10000; i++) {
            final String event = client.createEvent();
            final ProducerRecord<String, String> record = createProducerRecord(event);

            producer.send(record, createCallback());

            applyDelay();
        }
    }

    private static ProducerRecord<String, String> createProducerRecord(final String event) {

        return new ProducerRecord<>(KAFKA_TOPIC, null , event);
    }

    private static Callback createCallback() {

        return (recordMetadata, exception) -> {
            if (exception == null) {
                log.info(fillMetadataLogInfo(recordMetadata));
            } else {
                log.error("Error while producing!", exception);
            }
        };
    }

    private static String fillMetadataLogInfo(final RecordMetadata recordMetadata) {

        return "\nReceived new metadata. \n" +
                "Topic: " + recordMetadata.topic() + "\n" +
                "Partition: " + recordMetadata.partition() + "\n" +
                "Offset: " + recordMetadata.offset() + "\n";
    }

    private static void applyDelay() throws InterruptedException {

        Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
    }

    private static void shutdownHook(final KafkaProducer<String, String> producer) {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("shutting down the app..");
            producer.flush();
            producer.close();
        }));
    }

}
