package com.kafkastreams.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;

import java.time.Instant;

import static com.kafkastreams.configs.StreamsConfig.*;

@Data
@Slf4j
public class BankBalanceService {

    public static StreamsBuilder createStreamsBuilder() {

        return new StreamsBuilder();
    }

    public static KStream<String, JsonNode> createStream(final StreamsBuilder streamsBuilder) {

        return streamsBuilder.stream(KAFKA_STREAMS_INPUT_TOPIC, Consumed.with(STRING_SERDE, JSON_SERDE));
    }

    public static KTable<String, JsonNode>  streamFilter(final KStream<String, JsonNode> stream) {
        final ObjectNode initialBalance = createInitialBalance();

        final KGroupedStream<String, JsonNode> groupedStream = BankBalanceFilterService.groupByKey(stream);

        return BankBalanceFilterService.aggregateGroup(groupedStream, initialBalance);
    }

    private static ObjectNode createInitialBalance() {
        final ObjectNode initialBalance = JsonNodeFactory.instance.objectNode();
        final String time = Instant.ofEpochMilli(0L).toString();

        initialBalance.put("count", 0);
        initialBalance.put("balance", 0);
        initialBalance.put("timestamp", time);

        return initialBalance;
    }

    public static void writeTableToOutput(final KTable<String, JsonNode> table) {
        table.toStream().to(KAFKA_STREAMS_OUTPUT_TOPIC, Produced.with(STRING_SERDE, JSON_SERDE));
    }

    public static KafkaStreams createKafkaStream(final StreamsBuilder streamsBuilder) {

        return new KafkaStreams(streamsBuilder.build(), BankBalancePropertyService.setProperties());
    }

    public static void startKafkaStream(final KafkaStreams kafkaStreams) {
        kafkaStreams.cleanUp();
        kafkaStreams.start();
    }

    public static void gracefulShutDown(final KafkaStreams kafkaStreams) {
        log.info("Shutting down the app..");
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
