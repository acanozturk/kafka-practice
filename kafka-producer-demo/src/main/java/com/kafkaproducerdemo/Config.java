package com.kafkaproducerdemo;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class Config {

    public final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    public final static String KAFKA_TOPIC = "topic_1";
    public final static String CONSUMER_GROUP_ID = "my_first_application";

    public final static String SERIALIZER = StringSerializer.class.getName();
    public final static String DESERIALIZER = StringDeserializer.class.getName();

}
