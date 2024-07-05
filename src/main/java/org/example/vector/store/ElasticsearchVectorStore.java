package org.example.vector.store;
import cn.hutool.core.collection.CollectionUtil;
import co.elastic.clients.elasticsearch._types.Script;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.EmbeddingResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.ScriptType;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class ElasticsearchVectorStore implements IVectorStore {




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



//    public String retrieval(String collectionName,double[] vector) {
//        // Build the script,查询向量
//        Map<String, Object> params = new HashMap<>();
//        params.put("query_vector", vector);
//        // 计算cos值+1，避免出现负数的情况，得到结果后，实际score值在减1再计算
//        Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, "cosineSimilarity(params.query_vector, 'vector')+1", params);
//        ScriptScoreQueryBuilder scriptScoreQueryBuilder = new ScriptScoreQueryBuilder(QueryBuilders.boolQuery(), script);
//        // 构建请求
//        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
//                .withQuery(scriptScoreQueryBuilder)
//                .withPageable(Pageable.ofSize(3)).build();
//        SearchHits<ElasticVectorData> dataSearchHits = this.elasticsearchRestTemplate.search(nativeSearchQuery, ElasticVectorData.class, IndexCoordinates.of(collectionName));
//        //log.info("检索成功，size:{}", dataSearchHits.getTotalHits());
//        List<SearchHit<ElasticVectorData>> data = dataSearchHits.getSearchHits();
//        List<String> results = new LinkedList<>();
//        for (SearchHit<ElasticVectorData> ele : data) {
//            results.add(ele.getContent().getContent());
//        }
//        return CollectionUtil.join(results,"");
//    }


}
