# Java Concurrency Program Explanations with Source Code

## 1. PrintNumbersAndAlphabetsBruteForce
- **Purpose**: Prints numbers (1 to 26) and letters (A to Z) alternately using two threads.
- **Key Concepts**: Thread synchronization, `synchronized` blocks, `wait()`, `notify()`.
- **How It Works**:
  - Two `Runnable` tasks: one for letters, one for numbers.
  - A `synchronized` block with a shared `lock` ensures mutual exclusion.
  - A boolean flag (`printLetter`) controls alternation, with `wait()` pausing and `notify()` resuming threads.
  - Output: `A 1 B 2 ... Z 26`.
- **Use Case**: Basic thread coordination for ordered output.
- **Source Code**:
```java
package Threads;

public class PrintNumbersAndAlphabetsBruteForce {
    private static boolean printLetter = true;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        Runnable letterTask = new Runnable() {
            @Override
            public void run() {
                for(char ch = 'A'; ch <= 'Z'; ch++) {
                    synchronized (lock) {
                        if(!printLetter){
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.print(" "+ch);
                        printLetter = false;
                        lock.notify();
                    }
                }
            }
        };

        Runnable numberTask = new Runnable() {
            @Override
            public void run() {
                for (int num = 1; num <= 26; num++) {
                    synchronized (lock) {
                        if (printLetter) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.print(" " + num);
                        printLetter = true;
                        lock.notify();
                    }
                }
            }
        };

        Thread letterThread = new Thread(letterTask);
        Thread numberThread = new Thread(numberTask);

        letterThread.start();
        numberThread.start();

        try {
            letterThread.join();
            numberThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

## 2. CallableFutureRealWorldProblem
- **Purpose**: Executes multiple `Callable` tasks using `ExecutorService` to fetch simulated stock prices, retrieving results via `Future`.
- **Key Concepts**: `ExecutorService`, `Callable`, `Future`.
- **How It Works**:
  - A fixed thread pool submits `StockPriceFetcher` tasks for stock symbols.
  - Each task simulates a 5-second delay and returns a random price.
  - `Future.get()` retrieves results (blocking call).
  - The executor shuts down after task submission.
- **Use Case**: Parallel data fetching with result aggregation.
- **Source Code**:
```java
package Executors.CallableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableFutureRealWorldProblem {
    public static void main(String[] args) {
        List<String> symbols = List.of("ABC", "PQR", "TFGF", "YEDS", "PFS");
        List<Future> futures = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        for (String symbol : symbols) {
            Callable<Double> stockSymbolTask = new StockPriceFetcher(symbol);
            System.out.println("submitting for "+symbol);
            Future<Double> future = executorService.submit(stockSymbolTask);
            System.out.println("Future = "+future);
            futures.add(future);
        }
        executorService.shutdown();

        for(int i = 0 ;i<5;i++){
            try {
                System.out.println("Stock from " + symbols.get(i) + " price = " + futures.get(i).get() + " future status " + futures.get(i));
            } catch(InterruptedException | ExecutionException e){
                System.out.println(e);
            }
        }
    }
}

class StockPriceFetcher implements Callable<Double>{
    private String stockSymbol;
    public StockPriceFetcher(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    @Override
    public Double call() throws Exception {
        Thread.sleep(5000);
        return Math.random() * 100;
    }
}
```

## 3. MyBlockingQueue
- **Purpose**: Implements a custom thread-safe blocking queue using `ReentrantLock` and `Condition`.
- **Key Concepts**: `ReentrantLock`, `Condition`, producer-consumer pattern.
- **How It Works**:
  - A fixed-size queue with `put` and `take` methods.
  - `ReentrantLock` ensures thread safety; `Condition` objects (`notEmpty`, `notFull`) handle blocking.
  - Uses a circular buffer with `head` and `tail` indices.
- **Use Case**: Custom thread-safe data structures for producer-consumer scenarios.
- **Source Code**:
```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<T> {
    private Object[] items;
    private int capacity;
    private int head = 0;
    private int tail = 0;
    private int count = 0;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.items = new Object[capacity];
    }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (count == capacity) {
                notFull.await();
            }
            items[tail] = item;
            tail = (tail + 1) % capacity;
            count++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            T item = (T) items[head];
            items[head] = null;
            head = (head + 1) % capacity;
            count--;
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
```

## 4. ReentrantLockAdvantagesExample
- **Purpose**: Demonstrates `ReentrantLock` with timeout for thread-safe counter incrementation.
- **Key Concepts**: `ReentrantLock`, `tryLock`.
- **How It Works**:
  - Four threads increment a shared counter 10,000 times each.
  - `tryLock(1, TimeUnit.MILLISECONDS)` attempts to acquire the lock with a timeout.
  - Only threads acquiring the lock update the counter, ensuring thread safety.
- **Use Case**: Fine-grained locking with timeout for resource access.
- **Source Code**:
```java
package Threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class ReentrantLockAdvantagesExample {
    private static int counter = 0;
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        Runnable incrementTask = () -> {
            boolean lockAcquired = false;
            try {
                lockAcquired = lock.tryLock(1, TimeUnit.MILLISECONDS);
                if (lockAcquired) {
                    for (int i = 0; i < 10000; i++) {
                        counter++;
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " couldn't acquire the lock.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lockAcquired) {
                    lock.unlock();
                }
            }
        };

        Thread thread1 = new Thread(incrementTask);
        Thread thread2 = new Thread(incrementTask);
        Thread thread3 = new Thread(incrementTask);
        Thread thread4 = new Thread(incrementTask);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Counter (with Reentrant Lock): " + counter);
    }
}
```

## 5. CountDownLatchExample
- **Purpose**: Uses `CountDownLatch` to wait for multiple tasks to complete.
- **Key Concepts**: `CountDownLatch`.
- **How It Works**:
  - Three worker threads perform tasks and call `countDown()` when done.
  - The main thread waits at `latch.await()` until the latch count reaches zero.
  - Main thread proceeds after all workers finish.
- **Use Case**: Coordinating task completion, e.g., initialization.
- **Source Code**:
```java
import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        Runnable worker = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is working...");
                Thread.sleep(1000);
                latch.countDown();
                System.out.println(Thread.currentThread().getName() + " finished.");
            } catch (InterruptedException ignored) {}
        };

        new Thread(worker, "Worker-1").start();
        new Thread(worker, "Worker-2").start();
        new Thread(worker, "Worker-3").start();

        latch.await();
        System.out.println("All workers are done. Main thread proceeds.");
    }
}
```

## 6. CyclicBarrierExample
- **Purpose**: Synchronizes threads at a common point using `CyclicBarrier`.
- **Key Concepts**: `CyclicBarrier`.
- **How It Works**:
  - Three threads perform a task, then wait at `barrier.await()`.
  - When all threads reach the barrier, a `barrierAction` runs, and threads continue.
  - Barrier is reusable for multiple synchronization points.
- **Use Case**: Iterative algorithms requiring synchronized steps.
- **Source Code**:
```java
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {
    public static void main(String[] args) {
        Runnable barrierAction = () -> System.out.println("All threads reached the barrier. Proceeding...");

        CyclicBarrier barrier = new CyclicBarrier(3, barrierAction);

        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is doing part 1 work...");
                Thread.sleep(1000);
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " continues with part 2...");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        new Thread(task, "Thread-1").start();
        new Thread(task, "Thread-2").start();
        new Thread(task, "Thread-3").start();
    }
}
```

## 7. ProducerConsumerExample
- **Purpose**: Implements producer-consumer pattern with `ArrayBlockingQueue`.
- **Key Concepts**: `BlockingQueue`, producer-consumer.
- **How It Works**:
  - A producer adds items to a fixed-size queue (`put`), and a consumer removes them (`take`).
  - `ArrayBlockingQueue` handles synchronization, blocking when full or empty.
  - Random delays simulate real-world processing.
- **Use Case**: Data pipelines, message queues.
- **Source Code**:
```java
package Concurrency.ProducerConsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerExample {
    public static void main(String[] args) {
        BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(5);

        Thread producerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    buffer.put(i);
                    System.out.println("Produced " + i);
                    Thread.sleep((long) (Math.random() * 5000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Producer");

        Thread consumerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    int value = buffer.take();
                    System.out.println("Consumed " + value);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Consumer");

        producerThread.start();
        consumerThread.start();
    }
}
```

## 8. FuturevsCompletable
- **Purpose**: Compares `Future` and `CompletableFuture` for asynchronous task execution.
- **Key Concepts**: `Future`, `CompletableFuture`.
- **How It Works**:
  - `Future`: Submits a task via `ExecutorService`, retrieves result with blocking `get()`.
  - `CompletableFuture`: Uses `supplyAsync` for non-blocking execution, with `thenAccept` for result handling.
  - Main thread continues other tasks during `CompletableFuture` processing.
- **Use Case**: Non-blocking asynchronous operations.
- **Source Code**:
```java
import java.util.concurrent.*;

public class FuturevsCompletable {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(3000);
            return 42;
        });

        int resultFromFuture = future.get();
        System.out.println("Result from Future: " + resultFromFuture);

        executor.shutdown();

        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 42;
        });

        completableFuture.thenAccept(result -> {
            System.out.println("Result from CompletableFuture: " + result);
        });

        for (int i = 0; i < 5; i++) {
            System.out.println("Main thread executing task " + i);
            Thread.sleep(1000);
        }
    }
}
```

## 9. AtomicIntegerExample
- **Purpose**: Demonstrates thread-safe counter incrementation with `AtomicInteger`.
- **Key Concepts**: `AtomicInteger`, lock-free concurrency.
- **How It Works**:
  - Two threads increment an `AtomicInteger` 10,000 times each.
  - `incrementAndGet()` ensures atomic updates without locks.
  - Final counter value is consistent.
- **Use Case**: High-performance counters in multithreaded environments.
- **Source Code**:
```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerExample {
    private static AtomicInteger atomicCounter = new AtomicInteger(0);

    public static void main(String[] args) {
        Runnable atomicTask = () -> {
            for (int i = 0; i < 10000; i++) {
                atomicCounter.incrementAndGet();
            }
        };

        Thread thread1 = new Thread(atomicTask);
        Thread thread2 = new Thread(atomicTask);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
            System.out.println("Atomic Counter: " + atomicCounter.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

## 10. VolatileExample
- **Purpose**: Shows `volatile` for visibility in multithreaded programs.
- **Key Concepts**: `volatile`, visibility vs. atomicity.
- **How It Works**:
  - Two threads increment a `volatile` counter 10,000 times each.
  - `volatile` ensures visibility but not atomicity, potentially causing race conditions.
  - Final counter value may be inconsistent.
- **Use Case**: Visibility-critical scenarios, less reliable than `AtomicInteger`.
- **Source Code**:
```java
public class VolatileExample {
    private static volatile int sharedCounter = 0;

    public static void main(String[] args) {
        Runnable volatileTask = () -> {
            for (int i = 0; i < 10000; i++) {
                sharedCounter++;
            }
        };

        Thread thread1 = new Thread(volatileTask);
        Thread thread2 = new Thread(volatileTask);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
            System.out.println("Volatile Counter: " + sharedCounter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

## 11. SemaphoreDemo
- **Purpose**: Limits concurrent resource access using `Semaphore`.
- **Key Concepts**: `Semaphore`, resource control.
- **How It Works**:
  - A semaphore with 3 permits allows up to 3 threads in the critical section.
  - Five threads compete; `acquire()` and `release()` manage access.
  - Delays simulate resource usage.
- **Use Case**: Limiting concurrent access, e.g., database connections.
- **Source Code**:
```java
package Threads;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    private static final int NUM_THREADS = 5;
    private static final Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) {
        for (int i = 0; i < NUM_THREADS; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    Thread.sleep(3000);
                    System.out.println("Thread " + Thread.currentThread().getId() + " is in the critical section with "+ semaphore.availablePermits());
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }).start();
        }
    }
}
```

## 12. ConcurrentHashmapDemo
- **Purpose**: Demonstrates thread-safe operations on `ConcurrentHashMap`.
- **Key Concepts**: `ConcurrentHashMap`, thread-safe collections.
- **How It Works**:
  - Five threads insert key-value pairs concurrently.
  - `ConcurrentHashMap` ensures thread safety without explicit locking.
  - Final map size reflects all insertions.
- **Use Case**: Concurrent data storage in multithreaded applications.
- **Source Code**:
```java
package ConcurrentCollections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentHashmapDemo {
    private static final int NUM_THREADS = 5;
    private static final int NUM_INSERTIONS = 100;
    private static ConcurrentHashMap<String, Integer> hashMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.execute(insertRecord());
        }

        executorService.shutdown();

        if (!executorService.isTerminated()) {
            Thread.sleep(1000);
        }

        System.out.println("Size of the hashmap = " + hashMap.size());
    }

    private static Runnable insertRecord() {
        return () -> {
            for (int i = 0; i < NUM_INSERTIONS; i++) {
                hashMap.put(i + Thread.currentThread().getName(), i);
            }
        };
    }
}
```

## 13. ConcurrentFileReaderWithThreadPool
- **Purpose**: Reads multiple files concurrently using `ExecutorService`.
- **Key Concepts**: `ExecutorService`, thread pool, file I/O.
- **How It Works**:
  - A thread pool with 2 threads reads lines from multiple files.
  - Each task processes a file with delays to simulate work.
  - `ExecutorService` manages thread allocation and shutdown.
- **Use Case**: Parallel file processing, e.g., log analysis.
- **Source Code**:
```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentFileReaderWithThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        String[] filePaths = {
                "/Users/vd056735/samplelogs1.txt",
                "/Users/vd056735/samplelogs2.txt",
                "/Users/vd056735/samplelogs3.txt"
        };

        for(String filePath : filePaths){
            executorService.execute(() -> readFile(filePath));
        }
        executorService.shutdown();
    }

    private static void readFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Thread.sleep(4000);
                System.out.println("file path =" + filePath + " " + Thread.currentThread().getName() + ": reads line " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

## 14. ScheduledExecutorDemo
- **Purpose**: Schedules periodic tasks with `ScheduledExecutorService`.
- **Key Concepts**: `ScheduledExecutorService`, periodic tasks.
- **How It Works**:
  - A task runs every 3 seconds with a 3-second initial delay.
  - Prints current time and simulates work with a 2-second delay.
  - Program runs for 20 seconds before shutting down.
- **Use Case**: Periodic tasks, e.g., monitoring or polling.
- **Source Code**:
```java
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorDemo {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

        scheduler.scheduleAtFixedRate(()->{
            long currentTimeSeconds=System.currentTimeMillis()/1000;
            System.out.println("Task with fixed delay executed at:"+currentTimeSeconds+" seconds");
            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        },3,3,TimeUnit.SECONDS);

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        scheduler.shutdown();
    }
}
```