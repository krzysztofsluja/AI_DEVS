package org.sluja.aicourse.newClasses.utils.embedding;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class QdrantSearchService {

    private final QdrantClient myQdrantClient;
    private final EmbeddingService embeddingService;

    public List<Points.ScoredPoint> search(final String query, final String collection) throws ExecutionException, InterruptedException {
        return myQdrantClient
                .searchAsync(
                        Points.SearchPoints.newBuilder()
                                .setCollectionName(collection)
                                .addAllVector(getQueryEmbedding(query))
                                .setLimit(10)
                                .build())
                .get();
    }

    public List<String> getPayload(final String query, final String collection, final String payloadName) throws ExecutionException, InterruptedException {
        final List<Points.ScoredPoint> points = search(query, collection);
        return myQdrantClient.retrieveAsync(collection, points.stream().map(Points.ScoredPoint::getId).toList(), true, false, null)
                .get()
                .stream()
                .map(point -> point.getPayloadMap().get(payloadName).getStringValue())
                .toList();
    }

    private List<Float> getQueryEmbedding(final String query) {
        final float[] embedding = embeddingService.getEmbedding(query);
        final List<Float> embeddingList = new ArrayList<>();
        for(final float f : embedding) {
            embeddingList.add(f);
        }
        return embeddingList;
    }
}
