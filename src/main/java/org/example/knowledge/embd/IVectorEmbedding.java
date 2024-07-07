package org.example.knowledge.embd;

import org.example.domain.EmbeddingResult;

public interface IVectorEmbedding {

    EmbeddingResult embedding(String text);

}
