package implementation.multithreading;

import implementation.Solution;
import implementation.Solution.WordCount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import static implementation.Constants.*;

//This class handles the synchronization of the Producer and Consumer threads
public class Controller {
    private static Collection<Thread> producerThreadCollection, allThreadCollection;

    public static void start() {
        //entry point for multi-threaded producer consumer design
        //System.out.println("Starting:"+ start.toLocalTime());
        startDriverThreads();
        //System.out.println("Ending:"+ end.toLocalTime());
    }

    private static void startDriverThreads() {
        //main driver to start the producer and consumer threads
        producerThreadCollection = new ArrayList<Thread>();
        allThreadCollection = new ArrayList<Thread>();
        queue = new LinkedBlockingDeque<String>();
        //System.out.println("Starting P & C:"+LocalDateTime.now().toLocalTime());
        createAndStartProducers();
        createAndStartConsumers();
        //System.out.println("Ending P & C:"+LocalDateTime.now().toLocalTime());
        for (Thread t : allThreadCollection) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Solution.printOutput();
        //System.out.println("Task finished at"+LocalDateTime.now());

    }

    private static void createAndStartProducers() {
        //Driver to start the producer thread
        for (int i = 1; i <= NUMBER_OF_PRODUCERS; i++) {
            Producer producer = new Producer(queue);
            Thread producerThread = new Thread(producer, "producer-" + i);
            producerThreadCollection.add(producerThread);
            producerThread.start();
        }
        allThreadCollection.addAll(producerThreadCollection);
    }

    private static void createAndStartConsumers() {
        //Driver to start the consumer thread
        for (int i = 0; i < NUMBER_OF_CONSUMERS; i++) {
            Thread consumerThread = new Thread(new Consumer(queue), "consumer-" + i);
            allThreadCollection.add(consumerThread);
            consumerThread.start();
        }
    }

    public static boolean isProducerAlive() {
        //returns if the producer finished processing reads from the disk.
        for (Thread t : producerThreadCollection) {
            if (t.isAlive())
                return true;
        }
        return false;
    }
}
