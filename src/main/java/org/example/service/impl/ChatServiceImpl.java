package org.example.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.EmbeddingResult;
import org.example.domain.SearchedDocUnitResult;
import org.example.domain.UserSendMessage;
import org.example.entity.LawDocUnit;
import org.example.knowledge.embd.IKnowledgeEmbedding;
import org.example.knowledge.rerank.ISearchedReranker;
import org.example.knowledge.store.IKnowledgeStore;
import org.example.service.ChatService;
import org.example.service.UserContextService;
import org.example.web.WebRetriever;
import org.example.web.domain.WebRetrieveResult;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private IKnowledgeEmbedding embedding;

    @Autowired
    private IKnowledgeStore vectorStore;

    @Autowired
    private OllamaChatModel chatModel;

    @Autowired
    private ISearchedReranker reranker;

    @Autowired
    private WebRetriever webRetriever;

    private InMemoryChatMemory chatMemory = new InMemoryChatMemory();

    @Value("${app.chat.prompt.system}")
    private String systemPrompt;

    @Value("${app.chat.memory.size}")
    private Integer chatMemorySize;


    @Override
    public String chat(UserSendMessage userSendMessage) {

        String query = userSendMessage.getMessage();
        boolean useKnowledge = userSendMessage.isUseKnowledge();
        StringBuilder userQueryContext = new StringBuilder();

        StopWatch st = new StopWatch();

        if (useKnowledge) {
            st.start("embedding");
            EmbeddingResult embeddingResult = embedding.embedding(query);
            st.stop();
            st.start("retrieve");
            List<SearchedDocUnitResult<LawDocUnit>> results = vectorStore.retrieveByMix(query, embeddingResult.getEmbedding(), LawDocUnit.class);
            st.stop();
            // 当本地知识库无相关数据，从网络中获取数据
            if (CollectionUtil.isEmpty(results)) {
                WebRetrieveResult result = webRetriever.retrieve(query);
                String resultsStr = JSON.toJSONString(result.getResults());
                userQueryContext.append(resultsStr);
            } else {
                st.start("rerank");
                List<SearchedDocUnitResult<LawDocUnit>> rerankResult = reranker.rerank(results);
                st.stop();

                String queryResultStr = rerankResult.stream()
                        .map(SearchedDocUnitResult::getDocUnit)
                        .map((r) -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(r.getDocName()).append(r.getChapterName()).append(r.getUnitName()).append(r.getUnitContent());
                    return sb.toString();
                }).collect(Collectors.joining("\r\n"));
                userQueryContext.append(queryResultStr);
            }
        }

        String userPromptStr = STR."""
                用户问题：\{query}
                已有资料：\{userQueryContext.toString()}
                """;

        log.info("UserPrompt: {}", userPromptStr);

        st.start("LLM");
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory),new SimpleLoggerAdvisor())
                .build();
        String content = chatClient.prompt()
                .user(userPromptStr)
                .system(systemPrompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, userSendMessage.getUserId()).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, chatMemorySize))
                .call()
                .content();

        st.stop();
        log.info(st.prettyPrint());

        return content.toString();
    }


}
