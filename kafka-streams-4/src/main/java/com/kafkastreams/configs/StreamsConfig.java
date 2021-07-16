package com.kafkastreams.configs;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;

public class StreamsConfig {

    public final static String APPLICATION_ID = "bank_balance_app";
    public final static String BOOTSTRAP_SERVER = "127.0.0.1:9092";

    public final static String KAFKA_STREAMS_INPUT_TOPIC = "bank_balance_producer_topic";
    public final static String KAFKA_STREAMS_OUTPUT_TOPIC = "bank_balance_output_topic";

    final static Serializer<JsonNode> jsonNodeSerializer = new JsonSerializer();
    final static Deserializer<JsonNode> jsonNodeDeserializer = new JsonDeserializer();

    public final static Serde<String> STRING_SERDE = Serdes.String();
    public final static Serde<JsonNode> JSON_SERDE = Serdes.serdeFrom(jsonNodeSerializer, jsonNodeDeserializer);

}
