package com.kafkastreams3.configs;

import org.apache.kafka.common.serialization.Serdes;

public class KafkaConfig {

    public final static String APPLICATION_ID = "favorite_color_app";
    public final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    public final static String INPUT_KAFKA_TOPIC = "favorite_color_topic_input";
    public final static String FILTERED_KAFKA_TOPIC = "filtered_favorite_color_topic";
    public final static String OUTPUT_KAFKA_TOPIC = "favorite_color_topic_output";

    public final static String KEY_SERDE = Serdes.StringSerde.class.getName();
    public final static String VALUE_SERDE = Serdes.StringSerde.class.getName();

}
