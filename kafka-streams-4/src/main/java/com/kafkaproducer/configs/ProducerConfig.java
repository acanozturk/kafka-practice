package com.kafkaproducer.configs;

import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerConfig {

    public final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";

    public final static String KAFKA_PRODUCER_TOPIC = "bank_balance_producer_topic";

    public final static String SERIALIZER = StringSerializer.class.getName();

}
