package org.example.es;



import org.example.entity.LawDocUnit;
import org.example.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
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

    @Test
    public void query() {
        Criteria criteria = new Criteria("unitContent").matches("劳动");
        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<LawDocUnit> searchHits = elasticsearchOperations.search(query, LawDocUnit.class);
        for (SearchHit<LawDocUnit> searchHit : searchHits) {
            LawDocUnit lawDocUnit = searchHit.getContent();
            System.out.println("lawDocUnit = " + lawDocUnit.getUnitContent());
        }
    }



}
