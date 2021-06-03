package com.kafkaproducerdemo;

import org.apache.kafka.common.serialization.StringSerializer;

public class Config {

    public final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    public final static String KAFKA_TOPIC = "topic_1";

    public final static String SERIALIZER = StringSerializer.class.getName();

}
