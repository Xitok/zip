import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by TiM on 27.11.2016.
 */
public class Writer extends Thread {

    OutputStream outputStream;
    ArrayBlockingQueue<byte[]> queue;

    public Writer(OutputStream outputStream, ArrayBlockingQueue<byte[]> queue){
        this.outputStream = outputStream;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            byte[] bytes;
            while ((bytes = queue.take()).length > 0) {
                outputStream.write(bytes);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
