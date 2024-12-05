package org.sluja.aicourse.newClasses.tasks.S03E04.service;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task1.service.ChatService;
import org.sluja.aicourse.newClasses.tasks.S03E04.dtos.TextElement;
import org.sluja.aicourse.newClasses.utils.chat.ChatModelService;
import org.sluja.aicourse.newClasses.utils.webcontent.GetContentFromWebsiteService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetTextElementsService {

    private final ChatModelService chatModelService;
    private final GetContentFromWebsiteService getContentFromWebsiteService;
    private final String PROMPT_PATH = "prompts/text_elements_prompt.md";

    public enum Type {
        PEOPLE,
        PLACES;
    }

    public Map<String, List<String>> getTextElements() throws IOException {
        final String source = getContentFromWebsiteService.getContent("https://centrala.ag3nts.org/dane/barbara.txt");
        return Arrays.stream(Type.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        type -> {
                            try {
                                String response = chatModelService.getResponse(
                                        type.name(),
                                        PROMPT_PATH,
                                        source,
                                        "{prompt_text}"
                                );
                                return Arrays.asList(response.split(","));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ));
    }
}
