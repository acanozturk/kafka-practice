package com.kafkapractice;

import lombok.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class MockClient {

    public String createEvent() {
        final Message message = new Message();

        message.setUuid(UUID.randomUUID());
        message.setTimestamp(Calendar.getInstance().getTime());
        message.setMessage(randomSentence());

        final String[] event = {"date: " + message.getTimestamp().toString(),
                                "id: " + message.getUuid().toString(),
                                "text: " + message.getMessage()};

        return Arrays.toString(event);
    }

    private String randomSentence() {
        final String[] determiner = {"a", "the", "every", "some"};
        final String[] adjective = {"big", "tiny", "pretty", "bald"};
        final String[] common_noun = {"man", "woman", "fish", "elephant", "unicorn"};
        final String[] intransitive_verb = {"runs", "jumps", "talks", "sleeps"};
        final String[] proper_noun = {"red", "monkey", "baby", "bitcoin", "USA"};
        final String[] conjunction = {"and", "or", "but", "because", "hence"};
        final String[] transitive_verb = {"loves", "hates", "sees", "knows", "looks for", "finds"};

        int determinerLength = determiner.length;
        int adjectiveLength = adjective.length;
        int common_nounLength = common_noun.length;
        int intransitive_verbLength = intransitive_verb.length;
        int proper_nounLength = proper_noun.length;
        int conjunctionLength = conjunction.length;
        int transitive_verbLength = transitive_verb.length;

        int det = (int) (Math.random()*determinerLength);
        int adj = (int) (Math.random()*adjectiveLength);
        int cn = (int) (Math.random()*common_nounLength);
        int itv = (int) (Math.random()*intransitive_verbLength);
        int cj = (int) (Math.random()*conjunctionLength);
        int pn = (int) (Math.random()*proper_nounLength);
        int tv = (int) (Math.random()*transitive_verbLength);

        return determiner[det] + " " +
                adjective[adj] + " " +
                common_noun[cn] + " " +
                intransitive_verb[itv] + " " +
                conjunction[cj] + " " +
                proper_noun[pn] + " " +
                transitive_verb[tv];
    }
}
