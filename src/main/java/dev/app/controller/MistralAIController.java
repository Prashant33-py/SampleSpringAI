package dev.app.controller;

import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/mistral-ai")
public class MistralAIController {

    private final MistralAiChatModel chatModel;

    public MistralAIController(MistralAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping
    public String getAnthropicAIResponse(@RequestBody Map<String, String> prompt) {
        String response;
        try {
            response = chatModel.call(prompt.get("question"));
            return response;
        } catch (NonTransientAiException e){
            return e.getMessage().split(" - ")[1];
         }
    }

}
