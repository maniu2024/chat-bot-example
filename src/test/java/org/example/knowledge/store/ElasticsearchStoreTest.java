package org.example.knowledge.store;

import org.example.domain.EmbeddingResult;
import org.example.entity.LawDocUnit;
import org.example.knowledge.embd.IVectorEmbedding;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchStoreTest {

    @Autowired
    private IKnowledgeStore vectorStore;

    @Autowired
    private IVectorEmbedding embedding;

    @Test
    public void retrieveByVector() {

        String text = "hi";
        EmbeddingResult ret = embedding.embedding(text);
        vectorStore.retrieveByVector(ret.getEmbedding(), LawDocUnit.class);
    }
}