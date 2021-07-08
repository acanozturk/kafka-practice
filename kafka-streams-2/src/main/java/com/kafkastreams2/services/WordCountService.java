package com.kafkastreams2.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import static com.kafkastreams2.configs.KafkaConfig.FILTERED_KAFKA_TOPIC;
import static com.kafkastreams2.configs.KafkaConfig.KAFKA_TOPIC;

@Data
@Slf4j
public class WordCountService {

    public static StreamsBuilder createStreamsBuilder() {

        return new StreamsBuilder();
    }

    public static KStream<String, String> createStream(final StreamsBuilder streamsBuilder) {

        return streamsBuilder.stream(KAFKA_TOPIC);
    }

    public static KTable<String, Long> streamFilter(final KStream<String, String> stream) {
        final KStream<String, String> valuesToLowercase = FilterService.valuesToLowercase(stream);
        final KStream<String, String> splitValues = FilterService.splitValues(valuesToLowercase);
        final KStream<String, String> assignKeys = FilterService.assignKeys(splitValues);
        final KGroupedStream<String, String> groupByKey = FilterService.groupByKey(assignKeys);

        return FilterService.countKeys(groupByKey);
    }

    public static void writeFilteredStreamToKafka(final KTable<String, Long> filteredStream) {
        filteredStream.toStream().to(FILTERED_KAFKA_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
    }

    public static KafkaStreams createKafkaStream(final StreamsBuilder streamsBuilder) {

        return new KafkaStreams(streamsBuilder.build(), PropertyService.setProperties());
    }

    public static void startKafkaStream(final KafkaStreams kafkaStreams) {
        kafkaStreams.start();
        log.info(kafkaStreams.toString());
    }

    public static void gracefulShutDown(final KafkaStreams kafkaStreams) {
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
