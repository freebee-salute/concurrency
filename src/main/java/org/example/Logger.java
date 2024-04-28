package org.example;

public class Logger {

    protected static void logSubmissionsPerSecond(long startTime, int submissionsCount, int totalTasks) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        double submissionsPerSecond = (double) submissionsCount / (elapsedTime / 1000.0);
        System.out.print("\rElapsed time: " + elapsedTime + "ms | Submissions per second: "
                +  String.format("%.2f", submissionsPerSecond)
                + " | " + submissionsCount + "/" + totalTasks);
    }

    protected static void logCompletionsPerSecond(long startTime, int completedCount, int totalTasks) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        double completionsPerSecond = (double) completedCount / (elapsedTime / 1000.0);
        System.out.print("\rElapsed time: " + elapsedTime + "ms | Completions per second: "
                + String.format("%.2f", completionsPerSecond)
                + " | " + completedCount + "/" + totalTasks);
    }

    protected static void logEmptyLine() {
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();
    }
}
