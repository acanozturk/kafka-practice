package com.kafkaconsumerelasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class ConsumerElasticSearch {

    public static void main(String[] args) {
        final RestHighLevelClient client = createClient();
        final IndexRequest request = new IndexRequest("events", "messages");


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

}
