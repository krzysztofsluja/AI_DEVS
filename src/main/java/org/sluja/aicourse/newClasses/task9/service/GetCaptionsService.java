package org.sluja.aicourse.newClasses.task9.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.sluja.aicourse.S01.task1.service.WebScraperService;
import org.sluja.aicourse.S01.task5.service.TranscriptionService;
import org.sluja.aicourse.newClasses.utils.chat.ChatModelService;
import org.sluja.aicourse.newClasses.task9.dtos.Paragraph;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetCaptionsService {

    private final WebScraperService webScraperService;
    private final ChatModelService chatModelService;
    private final TranscriptionService transcriptionService;
    private final ObjectMapper objectMapper;
    private static final String PROMPT_PATH = "prompts/image_paragraph_description.md";
    private static final String OUTPUT_PATH = "src/main/resources/captions/article_captions.json";
    
    @Value("${task9.article.url}")
    private String ARTICLE_URL;

    public List<Paragraph> getCaptions() throws IOException {
        if(!loadCaptionsFromJson().isEmpty()) {
            return loadCaptionsFromJson();
        }
        final Document doc = webScraperService.getWebsite(ARTICLE_URL);
        Element currentH2 = null;
        Map<String, List<String>> h2ToParagraphsMap = new HashMap<>();
        
        for (Element element : doc.body().select("div.container").first().children()) {
            if (element.tagName().equals("h2")) {
                currentH2 = element;
                h2ToParagraphsMap.put(currentH2.text(), new ArrayList<>());
            } else if (element.tagName().equals("p") && currentH2 != null) {
                h2ToParagraphsMap.get(currentH2.text()).add(element.text());
            } else if (element.tagName().equals("figure") && currentH2 != null) {
                Element img = element.selectFirst("img");
                Element caption = element.selectFirst("figcaption");

                String imgSrc = img != null ? "https://centrala.ag3nts.org/dane/" + img.attr("src") : "No image source";
                String figCaption = caption != null ? caption.text() : "No caption";


                String figureContent = String.format("%s | <image_caption>%s</image_caption>",
                        imgSrc, figCaption);
                        
                h2ToParagraphsMap.get(currentH2.text()).add(figureContent);
            }
        }
        
        final List<Paragraph> par = h2ToParagraphsMap.keySet().stream()
                .map(h2 -> {
                    List<String> texts = h2ToParagraphsMap.get(h2);
                    String combinedText = String.join("\n\n", texts);

                    String processedText = combinedText;
                    
                    try {
                        // Process images if present
                        if (texts.stream().anyMatch(text -> text.contains("<image_caption>"))) {
                            String imageUrl = texts.stream()
                                    .filter(text -> text.contains("<image_caption>"))
                                    .map(text -> text.substring(0, text.indexOf(" | <image_caption>")))
                                    .findFirst()
                                    .orElse("");

                            processedText += "\n" + chatModelService.getResponse(combinedText, PROMPT_PATH, imageUrl, ChatModelService.FileType.URL);
                        }

                        // Process audio if present
                        if (texts.stream().anyMatch(text -> text.contains(".mp3"))) {
                            String audioUrl = texts.stream()
                                    .filter(text -> text.contains(".mp3"))
                                    .findFirst()
                                    .get();

                            String audioDescription = transcriptionService.transcribeAndGetFile(audioUrl, "files/9/");
                            processedText += "\n" + audioDescription;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    
                    return new Paragraph(h2, processedText);
                })
                .filter(p -> StringUtils.isNotBlank(p.text()))
                .toList();

        saveCaptionsToJson(par);
        return par;
    }

    private void saveCaptionsToJson(List<Paragraph> captions) throws IOException {
        File outputDir = new File(OUTPUT_PATH).getParentFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(OUTPUT_PATH), captions);
    }

    private List<Paragraph> loadCaptionsFromJson() throws IOException {
        File jsonFile = new File(OUTPUT_PATH);
        if (!jsonFile.exists()) {
            return List.of();
        }

        return objectMapper.readValue(
                jsonFile,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Paragraph.class)
        );
    }
}
