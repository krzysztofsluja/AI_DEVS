package org.sluja.aicourse.newClasses.task9.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.newClasses.task9.dtos.Paragraph;
import org.sluja.aicourse.newClasses.task9.dtos.ParagraphDescription;
import org.sluja.aicourse.newClasses.task9.service.GetCaptionsService;
import org.sluja.aicourse.newClasses.task9.service.GetParagraphDescriptionService;
import org.sluja.aicourse.newClasses.task9.service.Task9Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/9")
@RequiredArgsConstructor
public class Task9Controller {

    private final GetParagraphDescriptionService getParagraphDescriptionService;
    private final Task9Service task9Service;

    @GetMapping("/captions")
    public List<String> getCaptions() throws IOException {
        //List<ParagraphDescription> paragraphs = getParagraphDescriptionService.getSectionsWithDescriptions();
        return task9Service.getAnswers();
    }
}
