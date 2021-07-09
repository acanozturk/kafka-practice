package com.kafkastreams3.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import static com.kafkastreams3.configs.KafkaConfig.*;

@Data
@Slf4j
public class FavoriteColorService {

    public static StreamsBuilder createStreamsBuilder() {

        return new StreamsBuilder();
    }

    public static KStream<String, String> createStream(final StreamsBuilder streamsBuilder) {

        return streamsBuilder.stream(INPUT_KAFKA_TOPIC);
    }

    public static KStream<String, String> streamFilter(final KStream<String, String> stream) {
        final KStream<String, String> commaFilter = FilterService.filterValuesContainingComma(stream);
        final KStream<String, String> valuesToLowercase = FilterService.valuesToLowercase(commaFilter);
        final KStream<String, String> streamKeys = FilterService.splitAndGetKeys(valuesToLowercase);
        final KStream<String, String> streamValues = FilterService.splitAndGetValues(streamKeys);

        return FilterService.filterColors(streamValues);
    }

    public static void writeFilteredStreamToKafka(final KStream<String, String> filteredStream) {
        filteredStream.to(FILTERED_KAFKA_TOPIC);
    }

    public static KTable<String, String> createKTable(final StreamsBuilder streamsBuilder) {

        return streamsBuilder.table(FILTERED_KAFKA_TOPIC);
    }

    public static KTable<String, Long> getFavoriteColors(final KTable<String, String> table) {

        return FilterService.filterFavoriteColors(table);
    }

    public static void writeTableToOutput(final KTable<String, Long> table) {
        table.toStream().to(OUTPUT_KAFKA_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
    }

    public static KafkaStreams createKafkaStream(final StreamsBuilder streamsBuilder) {

        return new KafkaStreams(streamsBuilder.build(), PropertyService.setProperties());
    }

    public static void startKafkaStream(final KafkaStreams kafkaStreams) {
        kafkaStreams.start();
        log.info(kafkaStreams.toString());
    }

    public static void gracefulShutDown(final KafkaStreams kafkaStreams) {
        log.info("Shutting down the app..");
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
