package org.example.ollama;



import org.example.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ESSpringDataApiTest {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    @Test
    public void save() {
        Product product = new Product("bk-25", "City bike", 123.0);
        elasticsearchOperations.save(product);

    }



}
