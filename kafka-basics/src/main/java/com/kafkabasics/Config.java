package com.kafkabasics;

import lombok.Data;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

@Data
public class Config {

    public final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    public final static String KAFKA_TOPIC = "kafka_basics_topic";
    public final static String CONSUMER_GROUP_ID = "cg_kafka_basics";

    public final static String SERIALIZER = StringSerializer.class.getName();
    public final static String DESERIALIZER = StringDeserializer.class.getName();

}
