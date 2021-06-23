package com.kafkastreams;

import org.apache.kafka.common.serialization.Serdes;

public class Config {

    public final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    public final static String KAFKA_TOPIC = "kafka_cons_prod_topic";
    public final static String FILTERED_KAFKA_TOPIC = "filtered_streams_topic";
    public final static String APPLICATION_ID = "cg_kafka_streams";

    public final static String KEY_SERDE = Serdes.StringSerde.class.getName();
    public final static String VALUE_SERDE = Serdes.StringSerde.class.getName();

}
