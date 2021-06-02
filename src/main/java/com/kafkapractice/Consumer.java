package com.kafkapractice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static com.kafkapractice.Config.*;

@Data
@Slf4j
public class Consumer {

    public static void main(String[] args) {
        final Properties properties = setProperties();

        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singletonList(KAFKA_TOPIC));

        while(true) {
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            records.forEach(record -> log.info(fillRecordLogInfo(record)));
        }
    }

    private static Properties setProperties() {
        final Properties newProperties = new Properties();

        newProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        newProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, DESERIALIZER);
        newProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DESERIALIZER);
        newProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP);
        newProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return newProperties;
    }

    private static String fillRecordLogInfo(final ConsumerRecord<String, String> record) {

        return  "\nConsumed new data. \n" +
                "Topic: " + record.topic() + "\n" +
                "Partition: " + record.partition() + "\n" +
                "Offset: " + record.offset() + "\n" +
                "Key: " + record.key() + "\n" +
                "Value: " + record.value();
    }
}
