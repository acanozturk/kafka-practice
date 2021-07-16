package com.kafkastreams.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.apache.kafka.streams.kstream.*;

import java.time.Instant;

import static com.kafkastreams.configs.StreamsConfig.*;

@Data
public class BankBalanceFilterService {

    public static KGroupedStream<String, JsonNode> groupByKey(final KStream<String, JsonNode> stream) {

         return stream.groupByKey(Grouped.with(STRING_SERDE, JSON_SERDE));
    }

    public static KTable<String, JsonNode> aggregateGroup(final KGroupedStream<String, JsonNode> stream,
            final ObjectNode initialBalance) {

        return stream.aggregate(() -> initialBalance,
                (key, transaction, balance) -> createBalance(transaction, balance),
                Materialized.with(STRING_SERDE, JSON_SERDE));
    }

    private static JsonNode createBalance(final JsonNode transaction, final JsonNode balance) {
        final ObjectNode updatedBalance = JsonNodeFactory.instance.objectNode();

        final Integer count = getCount(balance);
        final Integer newBalance = getNewBalance(transaction, balance);
        final String epoch = getEpoch(transaction, balance);

        updatedBalance.put("count", count);
        updatedBalance.put("balance", newBalance);
        updatedBalance.put("timestamp", epoch);

        return updatedBalance;
    }

    private static Integer getCount(final JsonNode balance) {

        return balance.get("count").asInt() + 1;
    }

    private static Integer getNewBalance(final JsonNode transaction, final JsonNode balance) {
        final Integer oldBalance = balance.get("balance").asInt();
        final Integer transactionAmount = transaction.get("amount").asInt();

        return oldBalance + transactionAmount;
    }

    private static String getEpoch(final JsonNode transaction, final JsonNode balance) {
        final long balanceEpoch = Instant.parse(balance.get("timestamp").asText()).toEpochMilli();
        final long transactionEpoch = Instant.parse(transaction.get("timestamp").asText()).toEpochMilli();
        final Instant epoch = Instant.ofEpochMilli(Math.max(balanceEpoch, transactionEpoch));

        return epoch.toString();
    }
}
