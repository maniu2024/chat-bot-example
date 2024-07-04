package org.example.ollama;

import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Scanner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OllamaTest {

    public static void main(String[] args) {
        String model = "gemma:2b";

        var ollamaApi = new OllamaApi();
        var chatClient = new OllamaChatClient(ollamaApi).withModel(model)
                .withDefaultOptions(OllamaOptions.create()
                        .withModel(model)
                        .withTemperature(0.9f));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(">>> ");
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                break;
            }
            String resp = chatClient.call(message);
            System.out.println("<<< " + resp);
        }

    }


    @Resource
    private OllamaEmbeddingClient embeddingModel;


    @Test
    public void testEmbedding() {

        EmbeddingResponse embeddingResponse = embeddingModel.call(
                new EmbeddingRequest(List.of("Hello World", "World is big and salvation is near"),
                        OllamaOptions.create()
                                .withModel("mofanke/acge_text_embedding")));

        List<Embedding> results = embeddingResponse.getResults();

        results.forEach((res) -> {
            System.out.println(res.getOutput());
        });
        System.out.println("results = " + results);
    }




}
