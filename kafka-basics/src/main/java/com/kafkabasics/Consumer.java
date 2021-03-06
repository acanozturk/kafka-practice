package com.kafkabasics;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static com.kafkabasics.Config.*;

@Data
@Slf4j
public class Consumer {

    public static void main(String[] args) {
        final Properties properties = setProperties();

        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singletonList(KAFKA_TOPIC));

        final TopicPartition partition = new TopicPartition(KAFKA_TOPIC, 0);
        long partitionOffset = 15L;

//        int messagesRead = 0;
//        int messagesToRead = 5;
//        boolean readMessages = true;

//        consumer.assign(Collections.singletonList(partition));
//        consumer.seek(partition, partitionOffset);

        while(true) {
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            records.forEach(record -> log.info(fillRecordLogInfo(record)));
        }

//        while(readMessages) {
//            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
//
//            for(ConsumerRecord<String, String> record : records) {
//                log.info(fillRecordLogInfo(record));
//                messagesRead++;
//
//                if(messagesRead >= messagesToRead) {
//                    readMessages = false;
//                    break;
//                }
//            }
//        }
    }

    private static Properties setProperties() {
        final Properties newProperties = new Properties();

        newProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        newProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, DESERIALIZER);
        newProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DESERIALIZER);
        newProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
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
