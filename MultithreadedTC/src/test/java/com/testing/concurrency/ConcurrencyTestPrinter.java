package com.testing.concurrency;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrencyTestPrinter {
    private static int lastProgress = 0;

    public static void printThreadInfo(ConcurrentHashMap<ThreadInfo, Integer> threadInfoMap){
        Comparator<ThreadInfo> threadInfoComparator = Comparator.comparing(ThreadInfo::getCount)
                .thenComparing(ThreadInfo::getInstruction)
                .thenComparing(ThreadInfo::getThreadName)
                ;
        threadInfoMap.keySet().stream()
                .sorted(threadInfoComparator)
                .forEach(x -> System.out.println(x+":"+threadInfoMap.get(x)));
    }

    public static void printProgressBar(int currentIndex, int totalItems) {
        int progress = (int) (((double) currentIndex / totalItems) * 50);
        StringBuilder progressBar = new StringBuilder("[");
        int oldProgress = lastProgress;
        for (int i = 0; i < 50; i++) {
            if (i < progress) {
                progressBar.append("#");
                if(i>lastProgress)
                    lastProgress=i;

            } else {
                progressBar.append(" ");
            }
        }
        progressBar.append("]");
        System.out.print("\r" + progressBar + " " + (int) ((double) currentIndex / totalItems * 100) + "%");
        System.out.flush();  // Flush the output

        if (lastProgress > oldProgress) {
            System.out.println();  // Print a newline character when we're done
        }
    }


    public static String progressBar(int current, int total, int totalBars) {
        int ratio = current * totalBars / total;
        return new String(new char[ratio]).replace("\0", "=") +
                new String(new char[totalBars - ratio]).replace("\0", " ");
    }
}
