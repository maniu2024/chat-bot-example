package org.example.service.impl;

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
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserContextService userContextService;

    private InMemoryChatMemory chatMemory = new InMemoryChatMemory();


    @Override
    public String chat(UserSendMessage userSendMessage) {

        String text = userSendMessage.getMessage();
        boolean useKnowledge = userSendMessage.isUseKnowledge();

        String userPrompt = text;
        String systemPrompt = "你是一名专业的法律顾问助手，你会以专业的口吻解决用户的问题。";

        StopWatch st = new StopWatch();

        if (useKnowledge) {
            userPrompt = "下面是用户问题和已有的资料，你可以参考资料进行回复。\r\n";
            st.start("embedding");
            EmbeddingResult embeddingResult = embedding.embedding(text);
            st.stop();
            st.start("retrieve");
            List<SearchedDocUnitResult<LawDocUnit>> results = vectorStore.retrieveByMix(text, embeddingResult.getEmbedding(), LawDocUnit.class);
            st.stop();
            st.start("rerank");
            List<SearchedDocUnitResult<LawDocUnit>> rerankResult = reranker.rerank(results);
            st.stop();

            String queryResultStr = rerankResult.stream().map(SearchedDocUnitResult::getDocUnit).map((r) -> {
                StringBuilder sb = new StringBuilder();
                sb.append(r.getDocName()).append(r.getChapterName()).append(r.getUnitName()).append(r.getUnitContent());
                return sb.toString();
            }).collect(Collectors.joining("\r\n"));

            userPrompt += "已有资料：" + queryResultStr;

            log.info("userPrompt: {}", userPrompt);
        }

        st.start("LLM");
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory),new SimpleLoggerAdvisor())
                .build();
        String content = chatClient.prompt().user(userPrompt).system(systemPrompt).advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, userSendMessage.getUserId()).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)).call().content();

        st.stop();
        log.info(st.prettyPrint());

        return content.toString();
    }


}
