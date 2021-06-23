package com.kafkaproducerdemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.andreinc.mockneat.MockNeat;
import net.andreinc.mockneat.types.enums.MarkovChainType;

import java.util.Date;

import static net.andreinc.mockneat.types.enums.DomainSuffixType.POPULAR;
import static net.andreinc.mockneat.types.enums.HostNameType.ADVERB_VERB;
import static net.andreinc.mockneat.types.enums.URLSchemeType.HTTPS;

@Data
@EqualsAndHashCode
@ToString
public class MockClient {

    private final MockNeat mockNeat = MockNeat.threadLocal();

    public String createEvent() {
        final Gson gson = createGson();

        return mockNeat.
                reflect(Message.class)
                .field("url", mockNeat.urls().scheme(HTTPS).domain(POPULAR).host(ADVERB_VERB))
                .field("id", mockNeat.uuids())
                .field("city", mockNeat.cities().capitals().get())
                .field("user", mockNeat.names().full())
                .field("age", mockNeat.ints().range(15,85).get())
                .field("email", mockNeat.emails())
                .field("timestamp", new Date())
                .field("message", mockNeat.markovs().type(MarkovChainType.LOREM_IPSUM))
                .map(gson::toJson)
                .val();
    }

    private Gson createGson() {

        return new GsonBuilder().setPrettyPrinting().create();
    }
}
