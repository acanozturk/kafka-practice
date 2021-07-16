package com.kafkastreams;

import com.fasterxml.jackson.databind.JsonNode;
import com.kafkastreams.services.BankBalanceService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

@Data
@Slf4j
final class BankBalanceApp {

    public static void main(String[] args) {
        final StreamsBuilder streamsBuilder = BankBalanceService.createStreamsBuilder();
        final KStream<String, JsonNode> stream = BankBalanceService.createStream(streamsBuilder);
        final KTable<String, JsonNode> filteredStream = BankBalanceService.streamFilter(stream);

        BankBalanceService.writeTableToOutput(filteredStream);

        final KafkaStreams kafkaStream = BankBalanceService.createKafkaStream(streamsBuilder);

        BankBalanceService.startKafkaStream(kafkaStream);

        BankBalanceService.gracefulShutDown(kafkaStream);
    }

}
