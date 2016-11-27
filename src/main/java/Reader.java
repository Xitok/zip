import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by TiM on 27.11.2016.
 */
public class Reader extends Thread {

    InputStream inputStream;
    ArrayBlockingQueue<byte[]> queue;

    public Reader(InputStream inputStream, ArrayBlockingQueue<byte[]> queue){
        this.inputStream = inputStream;
        this.queue = queue;
    }

    @Override
    public void run() {
        byte[] bytes;
        int len;
        try {
            while ((len = inputStream.read(bytes = new byte[512])) > 0){
                if (len < 512){
                    byte[] lenByte = Arrays.copyOf(bytes, len);
                    queue.put(lenByte);
                } else {
                    queue.put(bytes);
                }
            }
            queue.put(new byte[0]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
