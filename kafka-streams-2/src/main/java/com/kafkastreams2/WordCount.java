package com.kafkastreams2;

import lombok.Data;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import static com.kafkastreams2.services.WordCountService.*;

@Data
final class WordCount {

    public static void main(String[] args) {
        final StreamsBuilder streamsBuilder = createStreamsBuilder();
        final KStream<String, String> stream = createStream(streamsBuilder);
        final KTable<String, Long> filteredStream = streamFilter(stream);

        writeFilteredStreamToKafka(filteredStream);

        final KafkaStreams kafkaStream = createKafkaStream(streamsBuilder);

        startKafkaStream(kafkaStream);

        gracefulShutDown(kafkaStream);
    }

}
