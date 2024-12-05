package org.sluja.aicourse.newClasses.tasks.S04E02.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sluja.aicourse.newClasses.tasks.S04E02.dtos.FineTunedDataObject;
import org.sluja.aicourse.newClasses.utils.files.GetLocalFilesService;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateFineTuneDataService {
    
    public static final String FINE_TUNE_DATA_PATH = "src/main/resources/finetuning/data";
    public static final String OUTPUT_FILE_PATH = "src/main/resources/finetuning/output/training_data.jsonl";
    
    public enum DataType {
        CORRECT,
        INCORRECT
    }
    
    private final GetLocalFilesService getLocalFilesService;
    private final ObjectMapper objectMapper;

    private List<String> getData(final DataType type) {
        final Map<String, String> allFiles = getLocalFilesService.getFilesWithTitle(FINE_TUNE_DATA_PATH);
        final String searchTerm = type == DataType.CORRECT ? "correct.txt" : "incorrect.txt";
        
        return allFiles.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(searchTerm))
                .map(Map.Entry::getValue)
                .flatMap(content -> Arrays.stream(content.split("\n")))
                .toList();
    }

    private List<FineTunedDataObject> getFineTunedData() {
        List<FineTunedDataObject> result = new ArrayList<>();
        
        result.addAll(FineTunedDataObject.createFromData(getData(DataType.CORRECT), DataType.CORRECT));
        result.addAll(FineTunedDataObject.createFromData(getData(DataType.INCORRECT), DataType.INCORRECT));
        
        return result;
    }

    public void saveFineTunedDataToJsonl() {
        List<FineTunedDataObject> data = getFineTunedData();
        Path outputPath = Paths.get(OUTPUT_FILE_PATH);
        
        // Create directories if they don't exist
        outputPath.getParent().toFile().mkdirs();
        
        try (FileWriter writer = new FileWriter(outputPath.toFile())) {
            for (FineTunedDataObject obj : data) {
                String jsonLine = objectMapper.writeValueAsString(obj);
                writer.write(jsonLine);
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save fine-tuned data to JSONL file", e);
        }
    }
} 