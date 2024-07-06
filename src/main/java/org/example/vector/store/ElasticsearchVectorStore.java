package org.example.vector.store;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.DocUnit;
import org.example.domain.EmbeddingResult;
import org.example.entity.LawDocUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.SearchTemplateQuery;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class ElasticsearchVectorStore implements IVectorStore {

    private final static String ID = "vector_query_id_4";
    private final static Integer TOP_N = 10;
    @Autowired
    private ElasticsearchOperations operations;

    public void store(String collectionName, List<EmbeddingResult> embeddingResults) {
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


    public List<DocUnit> retrieval(String collectionName, List<Double> embedding) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("vec", embedding);

        SearchTemplateQuery query = SearchTemplateQuery.builder().withId(ID).withParams(map).build();

        SearchHits<LawDocUnit> searchHits = operations.search(query, LawDocUnit.class);
        List<SearchHit<LawDocUnit>> searchHitsResult = searchHits.getSearchHits();

        return searchHitsResult.stream().limit(TOP_N).map((s) -> (DocUnit)s.getContent()).peek((d) ->{
            System.out.println("final retrieval doc:" + d.getUnitName() +  d.getUnitContent());
        }).toList();
    }


}
