package org.sluja.aicourse.newClasses.tasks.S03E04.controller;

import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.newClasses.tasks.S03E04.service.GetPlacesService;
import org.sluja.aicourse.newClasses.tasks.S03E04.service.GetTextElementsService;
import org.sluja.aicourse.newClasses.tasks.S03E04.service.Task14Service;
import org.sluja.aicourse.newClasses.utils.files.GetLocalFilesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/14")
@RequiredArgsConstructor
public class Task14Controller {

    private final Task14Service task14Service;
    private final GetLocalFilesService getLocalFilesService;

    @GetMapping("/elements")
    public List<String> task14() throws IOException {
        //return task14Service.report();
        getLocalFilesService.getFilesWithTitle("src/main/resources/correct_ft.txt");
        return null;
    }
}
