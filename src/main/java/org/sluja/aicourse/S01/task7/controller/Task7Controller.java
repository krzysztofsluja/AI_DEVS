package org.sluja.aicourse.S01.task7.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.S01.task7.dtos.RobotDescriptionJSONData;
import org.sluja.aicourse.S01.task7.service.GenerateImageProcessingDescriptionService;
import org.sluja.aicourse.S01.task7.service.ParametrizedApiResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/7")
public class Task7Controller {

    private final GenerateImageProcessingDescriptionService generateImageProcessingDescriptionService;
    private final ParametrizedApiResponseService parametrizedApiResponseService;

    @GetMapping("/desc")
    public ResponseEntity<String> getDescription() {
        return ResponseEntity.ok(parametrizedApiResponseService.getProcessedResponse("robotid", generateImageProcessingDescriptionService.generateImageAndGetUrl()));
    }
}
