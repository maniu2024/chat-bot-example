package org.example.es;

import jakarta.annotation.Resource;
import org.example.domain.EmbeddingResult;
import org.example.entity.LawDocUnit;
import org.example.vector.embd.IVectorEmbedding;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.SearchTemplateQuery;
import org.springframework.data.elasticsearch.core.script.Script;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ESTest {


    final static String ID = "vector_query_id_4";
    @Autowired
    private ElasticsearchOperations operations;
    @Resource
    private IVectorEmbedding embedding;

    @Test
    public void createQueryTemplate() {

        //  Import!!!!!
        //  query template ,if need input array in query ,template must use toJson,it can transfer it to array .
        // it is mustache style.

        String source = """
                {
                  "query": {
                    "function_score": {
                      "query": {
                        "match_all": {}
                      },
                      "functions": [
                        {
                          "script_score": {
                            "script": {
                              "source": "cosineSimilarity({{#toJson}}vec{{/toJson}}, 'contentVector') + 1.0"
                            }
                          }
                        }
                      ]
                    }
                  }
                }
                """;
        operations.putScript(Script.builder().withId(ID).withLanguage("mustache").withSource(source).build());
    }


    @Test
    public void testQueryWithTemplate() {

        String unitContent = "劳动合同中约定的试用期最长时间是多少?";
        EmbeddingResult result = embedding.embedding(unitContent);

        HashMap<String, Object> map = new HashMap<>();
        map.put("vec", result.getEmbedding());

        SearchTemplateQuery query = SearchTemplateQuery.builder().withId(ID).withParams(map).build();

        SearchHits<LawDocUnit> searchHits = operations.search(query, LawDocUnit.class);
        List<SearchHit<LawDocUnit>> searchHits1 = searchHits.getSearchHits();
        System.out.println(searchHits1);
    }


}
