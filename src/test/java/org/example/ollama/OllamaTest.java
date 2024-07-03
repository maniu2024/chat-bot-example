package org.example.ollama;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import java.util.Scanner;


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


}
