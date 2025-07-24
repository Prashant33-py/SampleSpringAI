package dev.app.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ollama-ai")
public class OllamaAiController {

    private final ChatClient chatClient;

    public OllamaAiController(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping
    public String getOllamaAiResponse(@RequestBody Map<String, String> prompt) {
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

    @PostMapping("/recommend")
    public String getMovieRecommendation(@RequestParam String genre, @RequestParam String year, @RequestParam String language) {

        String template = """
                I want to watch {genre} movies around the year {year} in {language} language.
                Can you recommend some movies for me? Suggest me only 1 movie with the length of the movie.
                The response should be in the following json format and it should contain title, length, cast, director and imdbRating.
                The cast should be a list of strings and the imdbRating should be a number.
                """;
        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre, "year", year, "language", language));

        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }

}
