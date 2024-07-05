package org.example.controller;

import org.example.domain.EmbeddingResult;
import org.example.vector.embd.IVectorEmbedding;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EmbeddingController {
 

    @Autowired
    private IVectorEmbedding embedding;
 
    @GetMapping("/ai/embedding")
    public EmbeddingResult embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return this.embedding.embedding(message);
    }
}