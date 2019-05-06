package implementation;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static implementation.Constants.*;

//This class handles the disk-reads by the producer thread and the word count processing by the consumer threads
public class Solution {
    private static ConcurrentHashMap<String, Integer> wordCountMap = new ConcurrentHashMap<>();
    private static PriorityQueue<WordCount> minHeapPQ = new PriorityQueue<>(k, new WordCountComparator());
    static String[] words;

    public static List<WordCount> getKMostFrequentWords(String filename) {
        //Entry point to handle Single threaded disk reading and data processing
        //brGetFrequentWords(k, filename);
        bufferedReaderGetFrequentWords(k, filename);
        return getResult(k);
    }

    private static void bufferedReaderGetFrequentWords(int k, String fileName) {
        //Reads data from the disk using BufferedReader for each line and updates the word count in the HashMap
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            // Reading the first line into currentLine
            String currentLine = br.readLine();

            while (currentLine != null) {

                // splitting the currentLine into word
                String[] words = currentLine.split(" ");

                for (String word : words) {
                    // if word is already present in wordCountMap, then update its count
                    if (wordCountMap.containsKey(word)) {
                        wordCountMap.put(word, wordCountMap.get(word) + 1);
                    }
                    // else insert the word as key and 1 as value
                    else {
                        wordCountMap.put(word, 1);
                    }
                }
                // Reading next line into currentLine
                currentLine = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

    private static void brGetFrequentWords(int k, String fileName) {
        //Reads data from the disk using BufferedReader for each line and updates the word count in the HashMap
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static List<WordCount> getResult(int k) {
        //Iterates across all keys and its count in a priority queue and retrieves the most frequent occurrence of k elements
        return getKFrequentWords(k);
    }

    private static List<WordCount> nioBufferedGetFrequentWords(int k, String fileName) {
        //Java NEW INPUT OUTPUT(NIO) to read data from disk
        Integer count = 0;
        try {
            RandomAccessFile aFile = new RandomAccessFile(fileName, "r");
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
            while (inChannel.read(buffer) > 0) {
                // System.out.println("S:" + LocalDateTime.now());
                buffer.flip();
                String content = new String(buffer.array(), "UTF-8");
                processLine(content);
                buffer.clear();
                count++;
                // System.out.println("E:" + LocalDateTime.now());
            }
            inChannel.close();
            aFile.close();
        } catch (IOException exc) {
            System.out.println(exc);
            System.exit(1);
        }
        //System.out.println("I/O:" + count);
        return getKFrequentWords(k);
    }


    private static List<WordCount> nioGetAllFrequentWords(int k, String fileName) {
        //Stores the entire file from disk to RAM
        //fails for size > RAM of the system
        //Hence not suitable for larger file size
        File file = new File(fileName);
        byte[] fileBytes = new byte[0];
        try {
            fileBytes = Files.readAllBytes(file.toPath());
            String content = new String(fileBytes, "UTF-8");
            processLine(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getKFrequentWords(k);
    }


    private static synchronized void callWorkerThreadsToProcessLine(String line) {
        //Synchronized block for Consumer threads to process words in each line from the queue
        words = line.split(" ");
        for (String word : words) {
            Integer count = wordCountMap.getOrDefault(word, 0);
            wordCountMap.put(word, count + 1);
            word = null;
            count = null;
        }
        words = null;
        line = null;
    }

    public static synchronized void processLine(String lineContent) {
        //Synchronized block for Consumer threads to process words in each line from the queue
        callWorkerThreadsToProcessLine(lineContent);
        lineContent = null;
    }

    private static List<WordCount> getKFrequentWords(int k) {
        //Iterates across all keys and its count in a priority queue and retrieves the most frequent occurrence of k elements
        //System.out.println("Starting the k:" + LocalDateTime.now().toLocalTime());
        for (Map.Entry<String, Integer> keyValue : wordCountMap.entrySet()) {
            if (minHeapPQ.size() >= k) {
                if (minHeapPQ.peek().getCount() < keyValue.getValue()) {
                    minHeapPQ.poll();
                    minHeapPQ.add(new WordCount(keyValue.getKey(), keyValue.getValue()));
                }
            } else
                minHeapPQ.add(new WordCount(keyValue.getKey(), keyValue.getValue()));
        }

        List<WordCount> kFreqWords = new ArrayList<>();

        while (!minHeapPQ.isEmpty()) {
            WordCount wc = minHeapPQ.poll();
            kFreqWords.add(new WordCount(wc.getWord(), wc.getCount()));
        }

        return kFreqWords;
    }

    public static void printOutput() {
        //Gets the output and prints it to the console
        end = LocalDateTime.now();
        System.out.println("Total execution time is " + ChronoUnit.SECONDS.between(start, end) + " seconds");
        System.out.println("The top 100 elements are ");
        System.out.println("Word\t\t\tFrequency");
        List<WordCount> result = Solution.getResult(k);
        String format = "%-20s%s";
        for (int i = result.size() - 1; i >= 0; i--)
            System.out.println(String.format(format, result.get(i).getWord(), result.get(i).getCount()));
    }

    public static class WordCount {
        String word;
        Integer count;

        public WordCount(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return this.word;
        }

        public Integer getCount() {
            return this.count;
        }
    }
}
