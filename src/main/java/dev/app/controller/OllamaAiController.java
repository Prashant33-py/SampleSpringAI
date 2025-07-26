package dev.app.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ollama-ai")
public class OllamaAiController {

    private final ChatClient chatClient;

    @Autowired
    private EmbeddingModel embeddingModel;

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

    /**
     * Get movie recommendations based on genre, year, and language.
     * @param genre The genre of the movie.
     * @param year The year the movie was released.
     * @param language The language of the movie.
     * @return A JSON string containing the recommended movie's title, length, cast, director, and IMDb rating.
     */
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

    /**
     * Get embeddings for a given text.
     * @param text The text to get embeddings for.
     * @return An array of floats representing the embeddings of the text.
     */
    @PostMapping("/embeddings")
    public float[] getEmbeddings(@RequestParam String text) {
        return embeddingModel
                .embed(text);
    }

    /**
     * Calculate the similarity between two texts using cosine similarity.<br/>
     * Refer the link {@link https://docs.spring.io/spring-ai/reference/api/vectordbs/understand-vectordbs.html#vectordbs-similarity} for more details on cosine similarity.
     *
     * @param text1 The first text to compare.
     * @param text2 The second text to compare.
     * @return A double representing the cosine similarity between the two texts, scaled to a percentage.
     */
    @PostMapping("/similarity")
    public double getSimilarity(@RequestParam String text1, @RequestParam String text2) {
        float[] embedding1 = embeddingModel.embed(text1);
        float[] embedding2 = embeddingModel.embed(text2);

        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (int i = 0; i < embedding1.length; i++) {
            dotProduct += embedding1[i] * embedding2[i];
            norm1 += embedding1[i] * embedding1[i];
            norm2 += embedding2[i] * embedding2[i];
        }
        return (dotProduct * 100) / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

}
