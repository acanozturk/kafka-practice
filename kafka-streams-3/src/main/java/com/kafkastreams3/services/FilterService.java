package com.kafkastreams3.services;

import lombok.Data;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.Arrays;

@Data
public class FilterService {

    public static KStream<String, String> filterValuesContainingComma(final KStream<String, String> stream) {

        return stream.filter((key, value) -> value.contains(","));
    }

    public static KStream<String, String> valuesToLowercase(final KStream<String, String> stream) {

        return stream.mapValues((ValueMapper<String, String>) String::toLowerCase);
    }

    public static KStream<String, String> splitAndGetKeys(final KStream<String, String> stream) {

        return stream.selectKey((key, value) -> value.split(",")[0]);
    }

    public static KStream<String, String> splitAndGetValues(final KStream<String, String> stream) {

        return stream.mapValues((key, value) -> value.split(",")[1]);
    }

    public static KStream<String, String> filterColors(final KStream<String, String> stream) {

        return stream.filter((key, value) -> Arrays.asList("red", "green", "blue").contains(value));
    }

    public static KTable<String, Long> filterFavoriteColors(final KTable<String, String> table) {

        return table.groupBy((key, value) -> createKeyValue(value, value)).count();
    }

    private static KeyValue<String, String> createKeyValue(final String key, final String value) {

        return new KeyValue<>(key, value);
    }

}
