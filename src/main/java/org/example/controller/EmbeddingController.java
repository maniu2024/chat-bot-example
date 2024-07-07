package org.example.controller;

import org.example.domain.EmbeddingResult;
import org.example.knowledge.embd.IKnowledgeEmbedding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmbeddingController {
 

    @Autowired
    private IKnowledgeEmbedding embedding;
 
    @GetMapping("/ai/embedding")
    public EmbeddingResult embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return this.embedding.embedding(message);
    }
}