package org.sluja.aicourse.newClasses.tasks.S03E04.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.sluja.aicourse.newClasses.tasks.S03E04.dtos.ApiResponseRecord;
import org.sluja.aicourse.newClasses.utils.chat.ChatModelService;
import org.sluja.aicourse.newClasses.utils.embedding.QdrantSearchService;
import org.sluja.aicourse.newClasses.utils.webcontent.GetContentFromWebsiteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GetPlacesService {

    private final GetContentFromWebsiteService getContentFromWebsiteService;
    private final GetTextElementsService getTextElementsService;
    private final ChatModelService chatModelService;
    private final QdrantSearchService qdrantSearchService;
    private final String PROMPT_PATH = "prompts/text_elements_validator_prompt.md";
    @Value("${task.api.key}")
    private String key;
    @Value("${people.api}")
    private String peopleApi;
    @Value("${places.api}")
    private String placesApi;

    public List<String> getPlaces() throws IOException {
        final Map<String, List<String>> textElements = getTextElementsService.getTextElements();
        final List<ApiResponseRecord> responses = new ArrayList<>();
        for(final String person : getPeople(textElements)) {
            responses.add(getContentFromWebsiteService.getContent(peopleApi, person.trim(), key, ApiResponseRecord.class));
        }
        final String mergedResponse = responses.stream()
                .map(apiResponse -> apiResponse.message().split(" "))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.joining(","));
        final String mergedResponseChatResponse = chatModelService.getResponse(mergedResponse, PROMPT_PATH);
        return Arrays.asList(mergedResponseChatResponse.split(","));
    }

    private List<String> getPeople(final Map<String, List<String>> textElements) throws IOException {
        final List<String> placeResponses = new ArrayList<>();
        for(final String place : textElements.get(GetTextElementsService.Type.PLACES.name())) {
            placeResponses.addAll(Arrays.stream(getContentFromWebsiteService.getContent(placesApi, place.trim(), key, ApiResponseRecord.class)
                            .message()
                            .split(" "))
                    .toList());
        }
        final List<String> peopleName = Stream.concat(textElements.get(GetTextElementsService.Type.PEOPLE.name()).stream(), placeResponses.stream()).toList();
        final String peopleList = peopleName.stream()
                .distinct()
                .map(person -> {
                    final String query = "Z jakimi postaciami miał/a powiązania " + person.trim() + "?";
                    try {
                        return qdrantSearchService.getPayload(query, "description", "person_name");
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.joining(","));
        return Arrays.asList(chatModelService.getResponse(peopleList, PROMPT_PATH).split(","));
    }
}
