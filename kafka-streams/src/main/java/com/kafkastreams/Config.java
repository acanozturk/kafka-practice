package com.kafkastreams;

import org.apache.kafka.common.serialization.Serdes;

public class Config {

    public final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    public final static String KAFKA_TOPIC = "kafka-streams-topic";
    public final static String APPLICATION_ID = "kafka-streams-demo";

    public final static String KEY_SERDE = Serdes.StringSerde.class.getName();
    public final static String VALUE_SERDE = Serdes.StringSerde.class.getName();

}
