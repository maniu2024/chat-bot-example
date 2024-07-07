package org.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.example.entity.Product;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class ESJavaApiTest {


    private ElasticsearchClient esClient;


    @Before
    public void init() {
        // URL and API key
        String serverUrl = "http://localhost:9200";
        String apiKey = "VnVhQ2ZHY0JDZGJrU...";

        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        this.esClient = new  ElasticsearchClient(transport);
    }

    @Test
    public void testCreate() throws IOException {
        esClient.indices().create(c -> c
                .index("products")
        );
    }


    @Test
    public void testIndex() throws IOException {
        Product product = new Product("bk-2", "City bike", 123.0);

        IndexResponse response = esClient.index(i -> i
                .index("products")
                .id(product.getSku())
                .document(product)
        );

        log.info("Indexed with version " + response.version());
    }

    @Test
    public void query() throws IOException {
        GetResponse<Product> response = esClient.get(g -> g
                        .index("products")
                        .id("bk-1"),
                Product.class
        );

        if (response.found()) {
            Product product = response.source();
            log.info("Product name " + product.getName());
        } else {
            log.info ("Product not found");
        }
    }

    @Test
    public void search() throws IOException {
        String searchText = "bike";

        SearchResponse<Product> response = esClient.search(s -> s
                        .index("products")
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query(searchText)
                                )
                        ),
                Product.class
        );
        log.info(response.hits().hits().toString());
    }

}
