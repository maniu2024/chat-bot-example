package org.example.vector.store;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.EmbeddingResult;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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


}
