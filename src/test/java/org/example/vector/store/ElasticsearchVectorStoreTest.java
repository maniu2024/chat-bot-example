package org.example.vector.store;

import org.example.domain.EmbeddingResult;
import org.example.vector.embd.IVectorEmbedding;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchVectorStoreTest {

    @Autowired
    private IVectorStore vectorStore;

    @Autowired
    private IVectorEmbedding embedding;

    @Test
    public void retrieval() {

        String text = "hi";
        EmbeddingResult ret = embedding.embedding(text);
        String retrieval = vectorStore.retrieval("", ret.getEmbedding().toArray(new Double[0]));
    }
}