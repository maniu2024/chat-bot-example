package org.example.vector.embd;

import org.example.domain.EmbeddingResult;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public class DefaultVectorEmbedding implements IVectorEmbedding {

    @Autowired
    private EmbeddingClient embeddingModel;

    @Override
    public EmbeddingResult embedding(String text) {
        List<Double> doubles = this.embeddingModel.embed(text);
        EmbeddingResult embeddingResult = new EmbeddingResult();
        embeddingResult.setText(text);
        embeddingResult.setEmbedding(doubles);
        return embeddingResult;
    }
}
