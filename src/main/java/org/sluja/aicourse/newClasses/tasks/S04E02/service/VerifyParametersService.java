package org.sluja.aicourse.newClasses.tasks.S04E02.service;

import org.sluja.aicourse.newClasses.tasks.S04E02.dtos.FineTunedDataObject;
import org.sluja.aicourse.newClasses.utils.chat.ChatModelService;
import org.sluja.aicourse.newClasses.utils.files.GetLocalFilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerifyParametersService {

    public static final String VERIFY_FILE_PATH = "src/main/resources/finetuning/verify";
    
    private final GetLocalFilesService getLocalFilesService;
    private final ChatModelService chatModelService;

    public Map<String, Map<String, String>> verifySequences() {
        Map<String, String> filesContent = getLocalFilesService.getFilesWithTitle(VERIFY_FILE_PATH);
        Map<String, Map<String, String>> results = new HashMap<>();
        
        for (Map.Entry<String, String> file : filesContent.entrySet()) {
            Map<String, String> fileResults = new HashMap<>();
            String[] lines = file.getValue().split("\n");
            
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    String rating = rateSequence(line.trim());
                    fileResults.put(line.trim(), rating);
                }
            }
            
            results.put(file.getKey(), fileResults);
        }
        
        return results;
    }
    
    private String rateSequence(String sequence) {
        return chatModelService.getResponseWithMessages(sequence, FineTunedDataObject.SYSTEM_MESSAGE);
    }
}
