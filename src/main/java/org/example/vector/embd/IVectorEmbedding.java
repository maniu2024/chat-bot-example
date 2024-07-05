package org.example.vector.embd;

import org.example.domain.EmbeddingResult;

public interface IVectorEmbedding {


    EmbeddingResult embedding(String text);

}
