package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        ExecutorService executionService = Executors.newFixedThreadPool(22);
        List<Future<Void>> futures = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        int totalTasks = 1000;
        AtomicInteger completedCount = new AtomicInteger(0);

        for (int i = 0; i < totalTasks; i++) {
            futures.add(executionService.submit(() -> {
                getResponseAsync();
                return null;
            }));
            Logger.logSubmissionsPerSecond(startTime, i + 1, totalTasks);
        }
        Logger.logEmptyLine();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            Logger.logCompletionsPerSecond(startTime, completedCount.get(), totalTasks);
        }, 1, 10, TimeUnit.MILLISECONDS);

        try {
            for (Future<Void> future : futures) {
                future.get();
                completedCount.getAndIncrement();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executionService.shutdown();
            scheduler.shutdown();
        }
    }

    private static void getResponseAsync() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://random-data-api.com/api/address/random_address")).GET().build();

        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).join(); // Wait for completion
        Writer.logToFile(response);
    }
}