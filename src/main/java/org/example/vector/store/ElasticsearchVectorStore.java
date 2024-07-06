package org.example.vector.store;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch._types.Script;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.EmbeddingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.search.querybuilder.QueryBuilders;

import java.util.*;

@Slf4j
@Component
public class ElasticsearchVectorStore implements IVectorStore {

    @Autowired
    private RestTemplate restTemplate;




    public void store(String collectionName, List<EmbeddingResult> embeddingResults){
        //保存向量
//        log.info("save vector,collection:{},size:{}",collectionName, CollectionUtil.size(embeddingResults));
//
//        List<IndexQuery> results = new ArrayList<>();
//        for (EmbeddingResult embeddingResult : embeddingResults) {
//            ElasticVectorData ele = new ElasticVectorData();
//            ele.setVector(embeddingResult.getEmbedding());
//            ele.setChunkId(embeddingResult.getRequestId());
//            ele.setContent(embeddingResult.getPrompt());
//            results.add(new IndexQueryBuilder().withObject(ele).build());
//        }
//        // 构建数据包
//        List<IndexedObjectInformation> bulkedResult = elasticsearchRestTemplate.bulkIndex(results, IndexCoordinates.of(collectionName));
//        int size = CollectionUtil.size(bulkedResult);
//        log.info("保存向量成功-size:{}", size);
    }



    public String retrieval(String collectionName,Double[] vector) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONArray vecArray = JSONUtil.parseArray(vector);

        String body = "{\n" +
                "  \"query\": {\n" +
                "    \"function_score\": {\n" +
                "      \"query\": {\n" +
                "        \"match_all\": {}\n" +
                "      },\n" +
                "      \"functions\": [\n" +
                "        {\n" +
                "          \"script_score\": {\n" +
                "            \"script\": {\n" +
                "              \"source\": \"cosineSimilarity(params.vec, 'contentVector') + 1.0\",\n" +
                "              \"params\": {\n" +
                "                \"vec\":  []          }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";

        JSONObject bean = JSONUtil.toBean(body, JSONObject.class, false);
        JSONObject paramsObj = bean.get("query",JSONObject.class).get("function_score",JSONObject.class)
                .get("functions",JSONArray.class).get(0,JSONObject.class)
                .get("script_score",JSONObject.class).get("script",JSONObject.class)
                .get("params",JSONObject.class);
        //JSONObject paramsObj = bean.getByPath("query.function_score.functions[0].script_score.script.params", JSONObject.class);
        paramsObj.set("vec",vecArray);
        bean.set("params",paramsObj);

        System.out.println("bean.toString() = " + bean.toString());





        HttpEntity<String> entity = new HttpEntity<>(bean.toString(), headers);
        ResponseEntity<String> exchange = restTemplate.exchange(
                "http://localhost:9200/law_doc_unit/_search",
                HttpMethod.POST,
                entity,
                String.class
        );

        return exchange.getBody();
    }


}
