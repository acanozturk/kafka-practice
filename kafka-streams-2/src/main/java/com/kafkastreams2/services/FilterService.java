package com.kafkastreams2.services;

import lombok.Data;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;

@Data
public class FilterService {

    public static KStream<String, String> valuesToLowercase(final KStream<String, String> stream) {

        return stream.mapValues((ValueMapper<String, String>) String::toLowerCase);
    }

    public static KStream<String, String> splitValues(final KStream<String, String> stream) {

        return stream.flatMapValues(values -> Arrays.asList(values.split(" ")));
    }

    public static KStream<String, String> assignKeys(final KStream<String, String> stream) {

        return stream.selectKey((key, value) -> value);
    }

    public static KGroupedStream<String, String> groupByKey(final KStream<String, String> stream) {

        return stream.groupByKey();
    }

    public static KTable<String, Long> countKeys(final KGroupedStream<String, String> stream) {

        return stream.count();
    }
}
