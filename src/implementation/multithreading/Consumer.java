package implementation.multithreading;

import implementation.Solution;

import java.util.concurrent.BlockingQueue;

//Main execution of the Consumer thread block
public class Consumer implements Runnable {

    private final BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            String line = queue.poll();

            if (line == null && !Controller.isProducerAlive())
                return;

            //if(line == null && producerFinished)
            //  return;

            if (line != null) {
                Solution.processLine(new String(line));
                //System.out.println(Thread.currentThread().getName()+" processing line: "+line);
            }
            line = null;
        }
    }
}
