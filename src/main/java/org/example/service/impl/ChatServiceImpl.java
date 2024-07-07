package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.DocUnit;
import org.example.domain.EmbeddingResult;
import org.example.domain.SearchedDocUnitResult;
import org.example.entity.LawDocUnit;
import org.example.knowledge.rerank.ISearchedReranker;
import org.example.service.ChatService;
import org.example.knowledge.embd.IVectorEmbedding;
import org.example.knowledge.store.IKnowledgeStore;
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
    private IKnowledgeStore vectorStore;

    @Autowired
    private OllamaChatClient chatClient;

    @Autowired
    private ISearchedReranker reranker;




    @Override
    public String chat(String text) {

        // todo add fulltext search
        EmbeddingResult embeddingResult = embedding.embedding(text);

        List<SearchedDocUnitResult<LawDocUnit>> results = vectorStore.retrieveByMix(text, embeddingResult.getEmbedding(), LawDocUnit.class);

        List<SearchedDocUnitResult<LawDocUnit>> rerankResult = reranker.rerank(results);


        String prompt = text;

        String queryResultStr = rerankResult.stream().map(SearchedDocUnitResult::getDocUnit)
                .map((r) ->{
                    StringBuilder sb = new StringBuilder();
                    sb.append(r.getUnitContent())
                            .append("\t该资料出自《")
                            .append(r.getDocName())
                            .append("》")
                            .append("中的")
                            .append(r.getChapterName())
                            .append("中的")
                            .append(r.getUnitName());
                    return sb.toString();
                }).collect(Collectors.joining("\r\n"));

        prompt += "\r\n请参考以下已有资料进行回答，已有资料：" + queryResultStr;

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
