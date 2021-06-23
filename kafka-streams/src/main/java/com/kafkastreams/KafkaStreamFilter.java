package com.kafkastreams;

import com.google.gson.JsonParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

import static com.kafkastreams.Config.*;

@Data
@Slf4j
public class KafkaStreamFilter {

    public static void main(String[] args) {
        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, String> stream = streamsBuilder.stream(KAFKA_TOPIC);
        final KStream<String, String> filteredStream = streamFilter(stream);

        filteredStream.to(FILTERED_KAFKA_TOPIC);

        final KafkaStreams kafkaStreams = createKafkaStreams(streamsBuilder);

        kafkaStreams.start();
    }

    private static Properties setProperties() {
        final Properties properties = new Properties();

        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, KEY_SERDE);
        properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, VALUE_SERDE);

        return properties;
    }

    private static Integer extractAge(final String value) {

        return JsonParser.parseString(value)
                .getAsJsonObject()
                .get("age")
                .getAsInt();
    }

    private static KStream<String, String> streamFilter(final KStream<String, String> stream) {

        return stream.filter(
                (key, value) -> extractAge(value) > 25 && extractAge(value) < 40
        );
    }

    private static KafkaStreams createKafkaStreams(final StreamsBuilder streamsBuilders) {

        return new KafkaStreams(streamsBuilders.build(), setProperties());
    }
}
