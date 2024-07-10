package org.example.knowledge.store;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.DocUnit;
import org.example.domain.SearchedDocUnitResult;
import org.example.enums.RetrievalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Component
public class ElasticsearchStore implements IKnowledgeStore {

    private final static String ID = "vector_query_id_4";

    /**
     * 每次检索的数量
     */
    private final static Integer TOP_N = 10;

    /**
     * 向量检索的结果的最低值要求，低于该值，直接过滤
     */
    private final static Double MIN_SCORE_VECTOR = 1.6;

    @Autowired
    private ElasticsearchOperations operations;

    private ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();


    @Override
    public <T extends DocUnit> void bulkStore(List<T> docUnits, Class<T> clazz) {
        List<IndexQuery> indexQueries = docUnits.stream().map((docUnit -> {
            return new IndexQueryBuilder().withObject(docUnit).build();
        })).toList();
        operations.bulkIndex(indexQueries, clazz);
    }

    public <T extends DocUnit> List<SearchedDocUnitResult<T>> retrieveByVector(List<Double> embedding, Class<T> clazz) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("vec", embedding);

        SearchTemplateQuery query = SearchTemplateQuery.builder().withId(ID).withParams(map).build();

        SearchHits<T> searchHits = operations.search(query, clazz);
        List<SearchHit<T>> searchHitsResult = searchHits.getSearchHits();

        return searchHitsResult.stream().limit(TOP_N)
                .filter((s) -> s.getScore() > MIN_SCORE_VECTOR)
                .map((s) -> {
                    T docUnit = s.getContent();
                    SearchedDocUnitResult<T> unitResult = new SearchedDocUnitResult<T>();
                    unitResult.setDocUnit(docUnit);
                    unitResult.setScore(s.getScore());
                    unitResult.setRetrievalType(RetrievalType.VECTOR);
                    return unitResult;
                })
                .peek((d) -> {
                    DocUnit docUnit = d.getDocUnit();
                    log.info("vector retrieval doc: {},{},{} ,{}", d.getScore(), docUnit.getChapterName(), docUnit.getUnitName(), docUnit.getUnitContent());
                }).toList();
    }

    @Override
    public <T extends DocUnit> List<SearchedDocUnitResult<T>> retrieveByMatch(String text, Class<T> clazz) {
        Criteria criteria = new Criteria("unitContent").matches(text);
        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<T> searchHits = operations.search(query, clazz);

        return searchHits.getSearchHits().stream().limit(TOP_N).map((s) -> {
                    T content = s.getContent();
                    SearchedDocUnitResult<T> result = new SearchedDocUnitResult<>();
                    result.setDocUnit(content);
                    result.setScore(s.getScore());
                    result.setRetrievalType(RetrievalType.MATCH);
                    return result;
                })
                .toList();
    }

    @Override
    public <T extends DocUnit> List<SearchedDocUnitResult<T>> retrieveByMix(String text, List<Double> embedding, Class<T> clazz) {
        Future<List<SearchedDocUnitResult<T>>> byVectorFuture = executorService.submit(() -> this.retrieveByVector(embedding, clazz));
        Future<List<SearchedDocUnitResult<T>>> byMatchFuture = executorService.submit(() -> this.retrieveByMatch(text, clazz));


        List<SearchedDocUnitResult<T>> byVectorResult = null;
        List<SearchedDocUnitResult<T>> fulltextResult = null;

        try {
            byVectorResult = byVectorFuture.get();
            fulltextResult = byMatchFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<SearchedDocUnitResult<T>> results = new ArrayList<>();

        // 如果向量检索结果为空，则不再考虑关键词检索结果
        if (CollectionUtil.isEmpty(byVectorResult)) {
            return results;
        }

        results.addAll(byVectorResult);
        results.addAll(fulltextResult);

        return results;
    }


}
