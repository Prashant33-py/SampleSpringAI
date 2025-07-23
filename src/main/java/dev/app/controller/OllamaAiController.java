package dev.app.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ollama-ai")
public class OllamaAiController {

    private final ChatClient chatClient;

    public OllamaAiController(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping
    public String getOllamaAiReponse(@RequestBody Map<String, String> prompt) {
        String response;
        try {
            response = chatClient
                    .prompt(prompt.get("question"))
                    .call()
                    .content();
            return response;
        } catch (NonTransientAiException e){
            return e.getMessage().split(" - ")[1];
         }
    }

}
