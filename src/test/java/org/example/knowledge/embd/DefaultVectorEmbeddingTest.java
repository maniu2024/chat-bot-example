package org.example.knowledge.embd;

import jakarta.annotation.Resource;
import org.example.domain.EmbeddingResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultVectorEmbeddingTest {

    @Resource
    private IVectorEmbedding embedding;

    @Test
    public void embedding() {
        String unitContent = "劳动合同中约定的试用期最长时间是多少?";
        EmbeddingResult result = embedding.embedding(unitContent);
        System.out.println("result.getEmbedding() = " + result.getEmbedding());

    }
}