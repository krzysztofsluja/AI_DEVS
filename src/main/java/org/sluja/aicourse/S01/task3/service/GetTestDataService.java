package org.sluja.aicourse.S01.task3.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sluja.aicourse.S01.task1.service.ChatService;
import org.sluja.aicourse.S01.task3.dto.TestDataElement;
import org.sluja.aicourse.S01.task3.dto.TestElement;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetTestDataService {
    
    private final FileDownloadService fileDownloadService;
    private final ExpressionEvaluator expressionEvaluator;
    private final ObjectMapper objectMapper;
    private final Path resourcesPath = Paths.get("src/main/resources/downloaded_document.txt");
    private final ChatService chatService;
    private final static String PROMPT = """
        Your main task is answering the given question. Follow that rules:
        - Your answer should be the most accurate possible
        - If it is possible give one word answer
        - Don't provide any description to your answer
        
        The question: {message}
    """;

    public GetTestDataService(FileDownloadService fileDownloadService, ChatService chatService) {
        this.fileDownloadService = fileDownloadService;
        this.expressionEvaluator = new ExpressionEvaluator();
        this.objectMapper = new ObjectMapper();
        this.chatService = chatService;
    }

    public List<TestDataElement> getTestData() throws IOException {
        fileDownloadService.downloadIfNotExists();
        JsonNode rootNode = objectMapper.readTree(resourcesPath.toFile());
        JsonNode testData = rootNode.get("test-data");
        
        List<TestDataElement> result = new ArrayList<>();
        
        for (JsonNode element : testData) {
            String question = element.get("question").asText();
            int operationResult = expressionEvaluator.evaluate(question);
            
            TestElement testElement = null;
            JsonNode testNode = element.get("test");
            if (testNode != null && !testNode.isMissingNode()) {
                final String ans = chatService.chat(testNode.get("q").asText(), PROMPT);
                testElement = new TestElement(
                    testNode.get("q").asText(),
                        ans
                );
            }
                
            result.add(new TestDataElement(
                question,
                operationResult,
                testElement
            ));
        }
        
        return result;
    }

    public String getDescription() throws IOException {
        JsonNode rootNode = objectMapper.readTree(resourcesPath.toFile());
        return rootNode.get("description").asText();
    }

    public String getCopyright() throws IOException {
        JsonNode rootNode = objectMapper.readTree(resourcesPath.toFile());
        return rootNode.get("copyright").asText();
    }

} 