package org.example.knowledge.store;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.EmbeddingResult;
import org.example.domain.SearchedDocUnitResult;
import org.example.entity.LawDocUnit;
import org.example.knowledge.embd.IKnowledgeEmbedding;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchStoreTest {

    @Autowired
    private IKnowledgeStore vectorStore;

    @Autowired
    private IKnowledgeEmbedding embedding;

    @Test
    public void retrieveByVector() {

        String text = "hi";
        EmbeddingResult ret = embedding.embedding(text);
        vectorStore.retrieveByVector(ret.getEmbedding(), LawDocUnit.class);
    }



    @Test
    public void retrieveByMatch() {
        List<SearchedDocUnitResult<LawDocUnit>> results = vectorStore.retrieveByMatch("劳动者有下列情形之一的，用人单位可以解除劳动合同：", LawDocUnit.class);
        results.forEach((e) -> {
            log.info(JSONUtil.toJsonStr(e));
        });
    }
}