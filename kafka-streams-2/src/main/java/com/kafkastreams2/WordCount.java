package com.kafkastreams2;

import com.kafkastreams2.services.WordCountService;
import lombok.Data;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

@Data
final class WordCount {

    public static void main(String[] args) {
        final StreamsBuilder streamsBuilder = WordCountService.createStreamsBuilder();
        final KStream<String, String> stream = WordCountService.createStream(streamsBuilder);
        final KTable<String, Long> filteredStream = WordCountService.streamFilter(stream);

        WordCountService.writeFilteredStreamToKafka(filteredStream);

        final KafkaStreams kafkaStream = WordCountService.createKafkaStream(streamsBuilder);

        WordCountService.startKafkaStream(kafkaStream);

        WordCountService.gracefulShutDown(kafkaStream);
    }

}
