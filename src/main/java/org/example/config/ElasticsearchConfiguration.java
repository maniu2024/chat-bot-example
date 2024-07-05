package org.example.config;

import co.elastic.clients.elasticsearch.nodes.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import static co.elastic.clients.elasticsearch.indices.get.Feature.Settings;

@Configuration
public class ElasticsearchConfiguration {


//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
//    //return new ElasticsearchTemplate();
//     }
}