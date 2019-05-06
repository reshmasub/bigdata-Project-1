package implementation.multithreading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;

import static implementation.Constants.BUFFER_CAPACITY;
import static implementation.Constants.fileName;

//Main execution of the Producer thread block
public class Producer implements Runnable {

    private final BlockingQueue<String> queue;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        callBR();
        //callNIO();
    }

    private void callBR() {
        //BUFFERED READER
        List<String> result = new ArrayList<>();
        BufferedReader br = null;
        String line;
        try {

            br = new BufferedReader(new FileReader(fileName), BUFFER_CAPACITY);
            while ((line = br.readLine()) != null) {
                //Reads data from disk and then puts each line into the queue to be processed by the consumers
                queue.put(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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

    private void callNIO() {
        // NIO
        try {
            RandomAccessFile aFile = new RandomAccessFile(fileName, "r");
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
            String content;
            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                content = new String(buffer.array(), "UTF-8");
                //System.out.println(Thread.currentThread().getName()+" producing line: "+ LocalDateTime.now().toLocalTime())
                StringTokenizer line = new StringTokenizer(content, "\n");
                String lineString;
                //Reads data from disk matching the given Buffer size and then puts each line into the queue to be processed by the consumers
                while (line.hasMoreTokens()) {
                    lineString = new String(line.nextToken());
                    if (lineString.length() > 0) {
                        queue.put(new String(lineString));
                    }
                    lineString = null;
                }
                line = null;
                content = null;
                buffer.clear(); // do something with the data and clear/compact it.
            }
            inChannel.close();
            aFile.close();
        } catch (IOException exc) {
            System.out.println(exc);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
