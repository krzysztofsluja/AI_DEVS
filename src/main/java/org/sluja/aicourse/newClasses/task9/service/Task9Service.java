package org.sluja.aicourse.newClasses.task9.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.sluja.aicourse.newClasses.task9.dtos.Paragraph;
import org.sluja.aicourse.newClasses.utils.chat.ChatModelService;
import org.sluja.aicourse.newClasses.utils.webcontent.GetContentFromWebsiteService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Task9Service {

    private final GetParagraphDescriptionService getParagraphDescriptionService;
    private final GetCaptionsService getCaptionsService;
    private final GetContentFromWebsiteService getContentFromWebsiteService;
    private final ChatModelService chatModelService;
    @Value("${task9.questions.url}")
    private String questionsUrl;
    private static final String JSON_PATH = "descriptions/paragraph_descriptions.json";
    private static final String PROMPT_PATH = "prompts/section_for_answer.md";
    private static final String ANSWER_PROMPT_PATH = "prompts/article_questions_answer.md";

    private String getQuestions() {
        return getContentFromWebsiteService.getContent(questionsUrl);
    }

    public String getSectionsForQuestions() throws IOException {
        final String questions = getQuestions();
        getParagraphDescriptionService.getSectionsWithDescriptions();
        String jsonContent = Files.readString(
                new ClassPathResource(JSON_PATH).getFile().toPath()
        );
        return chatModelService.getResponse(questions, PROMPT_PATH, jsonContent, "{section_description}");
    }

    public List<String> getAnswers() throws IOException {
        final List<Paragraph> paragraphs = getCaptionsService.getCaptions();
        final List<String> sections = Arrays.asList(getSectionsForQuestions().split("&&"));
        final List<String> questions = Arrays.asList(getQuestions().split("\n"));
        final Map<String, String> sectionsForQuestion = sections.stream().collect(Collectors.toMap(
                s -> s.substring(0, s.indexOf("=")).replace("\n", StringUtils.EMPTY),
                s -> {
                    String[] titles = s.substring(s.indexOf("=") + 1).trim().split("&");
                    return Arrays.stream(titles)
                            .map(title -> paragraphs.stream()
                                    .filter(p -> p.title().equalsIgnoreCase(title.trim()))
                                    .map(Paragraph::text)
                                    .findFirst()
                                    .orElse(""))
                            .filter(StringUtils::isNotBlank)
                            .collect(Collectors.joining("\n\n"));
                }
        ));

        return questions.stream().map(
                q -> {
                    String qNumber = q.substring(0, q.indexOf("="));
                    try {
                        return chatModelService.getResponse(q, ANSWER_PROMPT_PATH, sectionsForQuestion.get(qNumber), "{section}");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).toList();
    }
}
