package prime_nums.detector;

import prime_nums.checker.PrimeUtils;
import prime_nums.exception.ThreadException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadDetector implements NonPrimeDetector {
    private final int threadCount;

    public ThreadDetector(int threadCount) throws ThreadException {
        if (threadCount < 1) {
            throw new ThreadException("Thread count must be at least 1");
        }
        this.threadCount = threadCount;
    }

    @Override
    public boolean containsNonPrime(int[] numbers) {
        AtomicBoolean found = new AtomicBoolean(false);
        List<Thread> threads = new ArrayList<>();
        int len = numbers.length;
        int chunkSize = (len + threadCount - 1) / threadCount;

        for (int i = 0; i < threadCount; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, len);

            if (start >= len) break;

            Thread thread = new Thread(() -> {
               for (int j = start; j < end; j++) {
                   if (found.get()) return;
                   if (!PrimeUtils.isPrime(numbers[j])) {
                       found.set(true);
                       return;
                   }
               }
            });
            threads.add(thread);
            thread.start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return found.get();


    }
}
