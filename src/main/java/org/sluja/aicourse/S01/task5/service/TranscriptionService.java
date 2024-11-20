package org.sluja.aicourse.S01.task5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranscriptionService {

    private final DownloadAudioFilesFromDirectoryService downloadAudioFilesFromDirectoryService;
    private final OpenAiAudioTranscriptionModel whisperTranscriptionClient;
    private static final String TRANSCRIPTIONS_DIR = "src/main/resources/transcriptions";
    private static final Set<String> SUPPORTED_FORMATS = Set.of(
            "m4a", "mp3", "mp4", "mpeg", "mpga", "wav", "webm"
    );

    public void transcribe(final List<String> files) {
        createTranscriptionsDirectory();
        
        List<CompletableFuture<Void>> transcriptionFutures = files.stream()
                .filter(this::isSupportedFormat)
                .map(this::transcribeFileAsync)
                .toList();

        CompletableFuture.allOf(transcriptionFutures.toArray(new CompletableFuture[0]))
                .join();
        log.info("All transcriptions completed");
    }

    public String transcribeAndGetFile(final String audioFile, final String filePath) {
        try {
            log.info("Starting transcription for file: {}", audioFile);

            var fileResource = new ClassPathResource(filePath + audioFile);
            var transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                    .withModel("whisper-1")
                    .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                    .build();

            var transcriptionRequest = new AudioTranscriptionPrompt(fileResource, transcriptionOptions);
            var response = whisperTranscriptionClient.call(transcriptionRequest);

            String transcription = response.getResult().getOutput();
            log.info("Successfully transcribed file: {}", audioFile);
            return transcription;

        } catch (Exception e) {
            log.error("Failed to transcribe file: {}", audioFile, e);
            throw new RuntimeException("Failed to transcribe file: " + audioFile, e);
        }
    }

    public String transcribeAndGetFile(final String url) {
        try {
            log.info("Starting transcription for file: {}", url);

            var fileResource = new UrlResource(url);
            var transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                    .withModel("whisper-1")
                    .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                    .build();

            var transcriptionRequest = new AudioTranscriptionPrompt(fileResource, transcriptionOptions);
            var response = whisperTranscriptionClient.call(transcriptionRequest);

            String transcription = response.getResult().getOutput();
            log.info("Successfully transcribed file: {}", url);
            return transcription;

        } catch (Exception e) {
            log.error("Failed to transcribe file: {}", url, e);
            throw new RuntimeException("Failed to transcribe file: " + url, e);
        }
    }
    
    private boolean isSupportedFormat(String path) {
        String extension = getFileExtension(path);
        if (!SUPPORTED_FORMATS.contains(extension)) {
            log.warn("Unsupported file format for file: {}. Supported formats: {}", 
                    path, SUPPORTED_FORMATS);
            return false;
        }
        return true;
    }

    private String getFileExtension(String path) {
        int lastDotIndex = path.lastIndexOf('.');
        return lastDotIndex > 0 ? path.substring(lastDotIndex + 1).toLowerCase() : "";
    }
    
    private CompletableFuture<Void> transcribeFileAsync(final String audioFile) {
        return CompletableFuture.runAsync(() -> transcribeFile(audioFile))
                .exceptionally(throwable -> {
                    log.error("Async transcription failed for file: {}", audioFile, throwable);
                    return null;
                });
    }
    
    private void transcribeFile(final String audioFile) {
        try {
            log.info("Starting transcription for file: {}", audioFile);
            
            var fileResource = new ClassPathResource("files/task8/" + audioFile);
            var transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                    .withModel("whisper-1")
                    .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                    .build();
                    
            var transcriptionRequest = new AudioTranscriptionPrompt(fileResource, transcriptionOptions);
            var response = whisperTranscriptionClient.call(transcriptionRequest);
            
            String transcription = response.getResult().getOutput();
            saveTranscription(audioFile, transcription);
            
            log.info("Successfully transcribed file: {}", audioFile);
                    
        } catch (Exception e) {
            log.error("Failed to transcribe file: {}", audioFile, e);
            throw new RuntimeException("Failed to transcribe file: " + audioFile, e);
        }
    }

    private void createTranscriptionsDirectory() {
        try {
            Files.createDirectories(Paths.get(TRANSCRIPTIONS_DIR));
        } catch (IOException e) {
            log.error("Failed to create transcriptions directory", e);
            throw new RuntimeException("Failed to create transcriptions directory", e);
        }
    }

    private void saveTranscription(String audioFile, String transcription) {
        try {
            final String audioFileName = audioFile.substring(audioFile.lastIndexOf("/") + 1, audioFile.lastIndexOf("."));
            final Path transcriptionPath = Paths.get(TRANSCRIPTIONS_DIR, audioFileName + ".txt");
            Files.writeString(transcriptionPath, transcription);
            log.info("Saved transcription to: {}", transcriptionPath);
        } catch (IOException e) {
            log.error("Failed to save transcription for file: {}", audioFile, e);
            throw new RuntimeException("Failed to save transcription", e);
        }
    }
}
