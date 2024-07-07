package org.example.knowledge.embd;

import org.example.domain.EmbeddingResult;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultKnowledgeEmbedding implements IKnowledgeEmbedding {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Override
    public EmbeddingResult embedding(String text) {
        List<Double> doubles = this.embeddingModel.embed(text);
        EmbeddingResult embeddingResult = new EmbeddingResult();
        embeddingResult.setText(text);
        embeddingResult.setEmbedding(doubles);
        return embeddingResult;
    }
}
