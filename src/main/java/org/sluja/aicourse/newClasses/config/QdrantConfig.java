package org.sluja.aicourse.newClasses.config;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdrantConfig {

    @Value("${spring.ai.vectorstore.qdrant.api-key}")
    private String qdrantApiKey;

    @Value("${spring.ai.vectorstore.qdrant.host}")
    private String qdrantHost;
    @Bean
    public QdrantClient myQdrantClient() {
        return new QdrantClient(QdrantGrpcClient.newBuilder(qdrantHost, 6334, true)
                .withApiKey(qdrantApiKey)
                .build());
    }
}
