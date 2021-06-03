package com.kafkaconsumerelasticsearch;

import lombok.Data;
import org.apache.kafka.common.serialization.StringDeserializer;

@Data
public class Config {

    public final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    public final static String KAFKA_TOPIC = "topic_1";
    public final static String CONSUMER_GROUP_ID = "cg-elasticsearch-demo";

    public final static String DESERIALIZER = StringDeserializer.class.getName();

}
