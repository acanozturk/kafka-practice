package com.kafkastreams2.configs;

import org.apache.kafka.common.serialization.Serdes;

public class KafkaConfig {

    public final static String APPLICATION_ID = "word_count_app";
    public final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    public final static String KAFKA_TOPIC = "word_count_topic";
    public final static String FILTERED_KAFKA_TOPIC = "filtered_word_count_topic";

    public final static String KEY_SERDE = Serdes.StringSerde.class.getName();
    public final static String VALUE_SERDE = Serdes.StringSerde.class.getName();

}
