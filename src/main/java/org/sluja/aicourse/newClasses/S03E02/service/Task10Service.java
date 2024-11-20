package org.sluja.aicourse.newClasses.S03E02.service;

import com.google.protobuf.Descriptors;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Points;
import io.swagger.v3.oas.annotations.links.Link;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.signature.qual.FieldDescriptor;
import org.sluja.aicourse.newClasses.utils.embedding.EmbeddingService;
import org.sluja.aicourse.newClasses.utils.files.GetLocalFilesService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import static io.qdrant.client.ValueFactory.value;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.VectorsFactory.namedVectors;
import static io.qdrant.client.VectorsFactory.vectors;

@Service
@RequiredArgsConstructor
public class Task10Service {

    private final EmbeddingService embeddingService;
    private final GetLocalFilesService getLocalFilesService;
    private final QdrantClient myQdrantClient;


    public void saveEmbeddings() throws Exception {
        final Map<String, String> weaponTestsReports = getLocalFilesService.getFilesWithTitle("src/main/resources/weapon_test");
        final List<Points.PointStruct> points = weaponTestsReports.keySet()
                .stream()
                        .map(key -> {
                            final float[] embedding = embeddingService.getEmbedding(weaponTestsReports.get(key));
                            return Points.PointStruct.newBuilder()
                                    .setId(id(UUID.randomUUID()))
                                    .setVectors(vectors(embedding))
                                    .putAllPayload(Map.of("date", value(key)))
                                    .build();
                        })
                                .toList();
        myQdrantClient
                .upsertAsync("ksaidevs", points)
                .get();
    }

    public String getDate() throws ExecutionException, InterruptedException {
        final float[] embedding = embeddingService.getEmbedding("Znajduje się wzmianka o kradzieży prototypu broni?");
        final List<Float> embeddingList = new ArrayList<>();
        for (float f : embedding) {
            embeddingList.add(f);
        }
        final List<Points.ScoredPoint> points = myQdrantClient
                .searchAsync(
                        Points.SearchPoints.newBuilder()
                                .setCollectionName("ksaidevs")
                                .addAllVector(embeddingList)
                                .setLimit(1)
                                .build())
                .get();
        return myQdrantClient.retrieveAsync("ksaidevs", points.getFirst().getId() , true, false, null)
                .get()
                .getFirst()
                .getPayloadMap()
                .get("date")
                .getStringValue();


    }

}
