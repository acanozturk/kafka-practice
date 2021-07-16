package com.kafkaproducer.services;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

public class ProducerServiceTest {

    @Test
    public void createProducerRecord() {
        final ProducerRecord<String, String> record = ProducerService.createMockTransaction();

        final String key = record.key();
        final String value = record.value();

        System.out.println(key);
        System.out.println(value);
    }
}