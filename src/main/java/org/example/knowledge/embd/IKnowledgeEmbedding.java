package org.example.knowledge.embd;

import org.example.domain.EmbeddingResult;

public interface IKnowledgeEmbedding {

    EmbeddingResult embedding(String text);

}
