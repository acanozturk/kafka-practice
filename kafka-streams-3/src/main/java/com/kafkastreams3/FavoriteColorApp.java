package com.kafkastreams3;

import com.kafkastreams3.services.FavoriteColorService;
import lombok.Data;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

@Data
final class FavoriteColorApp {

    public static void main(String[] args) {
        final StreamsBuilder streamsBuilder = FavoriteColorService.createStreamsBuilder();
        final KStream<String, String> stream = FavoriteColorService.createStream(streamsBuilder);
        final KStream<String, String> filteredStream = FavoriteColorService.streamFilter(stream);

        FavoriteColorService.writeFilteredStreamToKafka(filteredStream);

        final KTable<String, String> table = FavoriteColorService.createKTable(streamsBuilder);
        final KTable<String, Long> favoriteColors = FavoriteColorService.getFavoriteColors(table);

        FavoriteColorService.writeTableToOutput(favoriteColors);

        final KafkaStreams kafkaStream = FavoriteColorService.createKafkaStream(streamsBuilder);

        FavoriteColorService.startKafkaStream(kafkaStream);

        FavoriteColorService.gracefulShutDown(kafkaStream);
    }

}
