package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.EmbeddingResult;
import org.example.domain.SearchedDocUnitResult;
import org.example.entity.LawDocUnit;
import org.example.knowledge.embd.IKnowledgeEmbedding;
import org.example.knowledge.rerank.ISearchedReranker;
import org.example.knowledge.store.IKnowledgeStore;
import org.example.service.ChatService;

import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private IKnowledgeEmbedding embedding;

    @Autowired
    private IKnowledgeStore vectorStore;

    @Autowired
    private OllamaChatModel chatClient;

    @Autowired
    private ISearchedReranker reranker;


    @Override
    public String chat(String text) {

        StopWatch st = new StopWatch();
        st.start("embedding");
        // todo add fulltext search
        EmbeddingResult embeddingResult = embedding.embedding(text);
        st.stop();
        st.start("retrieve");
        List<SearchedDocUnitResult<LawDocUnit>> results = vectorStore.retrieveByMix(text, embeddingResult.getEmbedding(), LawDocUnit.class);
        st.stop();
        st.start("rerank");
        List<SearchedDocUnitResult<LawDocUnit>> rerankResult = reranker.rerank(results);
        st.stop();
        String userPrompt = text;

        String queryResultStr = rerankResult.stream().map(SearchedDocUnitResult::getDocUnit)
                .map((r) -> {
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

        userPrompt += "已有资料：" + queryResultStr;

        log.info("userPrompt: {}", userPrompt);
        st.start("LLM");

        UserMessage userMessage = new UserMessage(userPrompt);
        SystemMessage systemMessage = new SystemMessage("请参考以下已有资料进行回答,如果参考了资料，需要在回答中返回参考的资料出处.如果提供的资料和查询内容不相关，你可以选择不参考。");
        List<Message> messages = List.of(systemMessage, userMessage);

        ChatResponse response = chatClient.call(
                new Prompt(messages));
        st.stop();

        String content = response.getResult().getOutput().getContent();
        log.info(st.prettyPrint());
        return content;
    }


}
