package com.kafkaproducer.services;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.andreinc.mockneat.MockNeat;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import static com.kafkaproducer.configs.ProducerConfig.KAFKA_PRODUCER_TOPIC;

@Data
@Slf4j
public class ProducerService {

    private static final MockNeat mockNeat = MockNeat.threadLocal();

    public static KafkaProducer<String, String> createKafkaProducer() {

        return new KafkaProducer<>(ProducerPropertyService.setProperties());
    }

    public static void startEventProducing(final KafkaProducer<String, String> producer)
            throws InterruptedException {

        for(int i=0; i<10000; i++) {
            final ProducerRecord<String, String> record = createMockTransaction();

            producer.send(record, createCallback());

            applyDelay();
        }
    }

    public static ProducerRecord<String, String> createMockTransaction() {
        final ObjectNode transaction = JsonNodeFactory.instance.objectNode();

        final String name = mockNeat.names().first().valStr();
        final String timestamp = Instant.now().toString();
        final Double amount = ThreadLocalRandom.current().nextDouble(1, 99999);

        transaction.put("name", name);
        transaction.put("amount", amount);
        transaction.put("timestamp", timestamp);

        return new ProducerRecord<>(KAFKA_PRODUCER_TOPIC, name, transaction.toString());
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

    public static void shutdownHook(final KafkaProducer<String, String> producer) {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down the app..");
            producer.flush();
            producer.close();
        }));
    }
}
