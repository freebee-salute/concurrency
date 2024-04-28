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
    public static HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    public static HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/hello")).GET().build();

    public static void main(String[] args) {
        int poolSize = Runtime.getRuntime().availableProcessors() * (1 + 10 / 5);
        System.out.println("PoolSize: " + poolSize);
        ExecutorService executionService = Executors.newFixedThreadPool(2);
        List<Future<Void>> futures = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        int totalTasks = 10000;
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
        }, 1, 100, TimeUnit.MILLISECONDS);

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
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(Writer::logToFile); // Wait for completion
    }
}