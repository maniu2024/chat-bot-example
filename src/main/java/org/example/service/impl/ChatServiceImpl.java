package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.DocUnit;
import org.example.domain.EmbeddingResult;
import org.example.service.ChatService;
import org.example.vector.embd.IVectorEmbedding;
import org.example.vector.store.IVectorStore;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private IVectorEmbedding embedding;

    @Autowired
    private IVectorStore vectorStore;

    @Autowired
    private OllamaChatClient chatClient;

    @Override
    public String chat(String text) {
        // todo add fulltext search
        EmbeddingResult embeddingResult = embedding.embedding(text);

        // dense query
        List<DocUnit> queryResult = vectorStore.retrieval("", embeddingResult.getEmbedding());

        // todo rerank
        String prompt = text;

        String queryResultStr = queryResult.stream().map(DocUnit::getUnitContent).collect(Collectors.joining("\r\n"));

        prompt += "请参考以下已有事实内容进行回答，已有事实：" + queryResultStr;

        log.info("prompt: {}", prompt);
        ChatResponse response = chatClient.call(
                new Prompt(
                        prompt,
                        OllamaOptions.create()
                                .withModel("gemma:2b")
                                .withTemperature(0.4f)
                ));

        String content = response.getResult().getOutput().getContent();
        return content;
    }


}
