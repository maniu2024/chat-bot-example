package org.example.vector.embd;

import org.example.domain.EmbeddingResult;

import java.util.List;

public interface IVectorEmbedding {

    EmbeddingResult embedding(String text);

}
