package com.kafkaproducer;

import com.kafkaproducer.services.ProducerService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;

@Data
@Slf4j
public class ProducerApp {

    public static void main(String[] args) throws InterruptedException {
        final KafkaProducer<String, String> producer = ProducerService.createKafkaProducer();

        ProducerService.startEventProducing(producer);

        ProducerService.shutdownHook(producer);
    }

}
