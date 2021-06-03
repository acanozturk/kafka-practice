package com.kafkaconsumerelasticsearch;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static com.kafkaconsumerelasticsearch.Config.*;

@Data
@Slf4j
public class ConsumerElasticSearch {

    public static void main(String[] args) throws IOException, InterruptedException {
        final RestHighLevelClient client = createClient();
        final KafkaConsumer<String, String> consumer = createConsumer();

        consumer.subscribe(Collections.singletonList(KAFKA_TOPIC));

        while(true) {
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String, String> record : records) {
                final IndexRequest request = new IndexRequest("consumer", "events")
                        .source(record.value(), XContentType.JSON);
                final IndexResponse response = client.index(request, RequestOptions.DEFAULT);

                log.info(response.getId());

                Thread.sleep(1000);
            }
        }

        //client.close();
    }

    public static RestHighLevelClient createClient() {
        final String host = "kafka-consumer-demo-5857859532.eu-central-1.bonsaisearch.net";
        final String username = "qz0rwz31w6";
        final String password = "auulowr820";

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);

        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        final HttpHost httpHost = new HttpHost(host, 443, "https");

        final RestClientBuilder.HttpClientConfigCallback callback = httpAsyncClientBuilder ->
                httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

        final RestClientBuilder restClientBuilder = RestClient.builder(httpHost).setHttpClientConfigCallback(callback);

        return new RestHighLevelClient(restClientBuilder);
    }

    private static KafkaConsumer<String, String> createConsumer() {
        final Properties properties = setProperties();

        return new KafkaConsumer<>(properties);
    }

    private static Properties setProperties() {
        final Properties newProperties = new Properties();

        newProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        newProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, DESERIALIZER);
        newProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DESERIALIZER);
        newProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
        newProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return newProperties;
    }

}
