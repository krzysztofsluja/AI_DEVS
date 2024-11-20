package org.sluja.aicourse.newClasses.task9.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.newClasses.utils.chat.ChatModelService;
import org.sluja.aicourse.newClasses.task9.dtos.Paragraph;
import org.sluja.aicourse.newClasses.task9.dtos.ParagraphDescription;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetParagraphDescriptionService {

    private final GetCaptionsService getCaptionsService;
    private final ChatModelService chatModelService;
    private final ObjectMapper objectMapper;
    
    private static final String SECTION_PROMPT_PATH = "prompts/paragraph_summary.md";
    private static final String OUTPUT_PATH = "src/main/resources/descriptions/paragraph_descriptions.json";

    public List<ParagraphDescription> getSectionsWithDescriptions() throws IOException {
        final List<ParagraphDescription> existingDescriptions = loadDescriptionsFromJson();
        if(!existingDescriptions.isEmpty()) {
            return existingDescriptions;
        }

        List<Paragraph> sections = getCaptionsService.getCaptions();
        
        // Create CompletableFuture for each section
        List<CompletableFuture<ParagraphDescription>> futures = sections.stream()
                .map(section -> CompletableFuture.supplyAsync(() -> {
                    try {
                        String sectionPrompt = String.format(
                            "Title: %s\n\nContent: %s\n\nPlease provide a comprehensive summary of this section.",
                            section.title(),
                            section.text()
                        );
                        
                        String description = chatModelService.getResponse(sectionPrompt, SECTION_PROMPT_PATH);
                        
                        return new ParagraphDescription(section.title(), description);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to generate description for section: " + section.title(), e);
                    }
                }))
                .toList();

        // Wait for all futures to complete and collect results
        List<ParagraphDescription> descriptions = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException("Error processing section description", e);
                    }
                })
                .collect(Collectors.toList());

        saveDescriptionsToJson(descriptions);
        
        return descriptions;
    }
    
    private void saveDescriptionsToJson(List<ParagraphDescription> descriptions) throws IOException {
        File outputDir = new File(OUTPUT_PATH).getParentFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(OUTPUT_PATH), descriptions);
    }
    
    public List<ParagraphDescription> loadDescriptionsFromJson() throws IOException {
        File jsonFile = new File(OUTPUT_PATH);
        if (!jsonFile.exists()) {
            return List.of();
        }
        
        return objectMapper.readValue(
            jsonFile,
            objectMapper.getTypeFactory().constructCollectionType(List.class, ParagraphDescription.class)
        );
    }
}
