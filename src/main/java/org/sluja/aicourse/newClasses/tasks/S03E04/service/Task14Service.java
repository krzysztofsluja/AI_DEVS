package org.sluja.aicourse.newClasses.tasks.S03E04.service;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.newClasses.utils.webcontent.GetContentFromWebsiteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Task14Service {

    private final GetContentFromWebsiteService getContentFromWebsiteService;
    private final GetPlacesService getPlacesService;
    @Value("${task.api.key}")
    private String key;

    public List<String> report() throws IOException {
        return getPlacesService.getPlaces().stream()
                .map(this::getContentWithFallback)
                .toList();
    }

    public String getContentWithFallback(final String city) {
        try {
            return getContentFromWebsiteService.reportAnswer("loop",key, city);
        } catch (HttpClientErrorException.Forbidden e) {
            return "Access Forbidden: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
