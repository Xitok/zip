import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by TiM on 24.11.2016.
 */
public class ZipFile extends Thread {

    private ZipOutputStream zOut;
    private FileInputStream file;
    private String zipName;
    private String[] files;

    public ZipFile(String zipName, String[] files) throws FileNotFoundException {
        System.out.println("Zip name: " + zipName);
        System.out.println("Files to zip:");
        for (String fileName: files){
            System.out.println(fileName);
        }
        this.files = files;
        this.zipName = zipName;
    }

    @Override
    public void run() {
        BlockedFiles bf = BlockedFiles.getInstance();
        if (bf.addFilesToBlock(files)){
            if (bf.addZipToBlock(zipName)){
                try {
                    this.zOut = new ZipOutputStream(new FileOutputStream(zipName));
                    zip();
                } catch (IOException e) {
                    System.out.println("Zip error");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    bf.removeFiles(files);
                    bf.removeZip(zipName);
                }
                System.out.println("Zip complete");
            } else {
                System.out.println("File in work");
            }
        } else {
            System.out.println("Files in work");
        }
    }

    public void zip() throws IOException, InterruptedException {
        System.out.println("Zip start");
        for (String fileName: files){
            ArrayBlockingQueue<byte[]> queue = new ArrayBlockingQueue<byte[]>(10);
            long start = System.nanoTime();
            file = new FileInputStream(fileName);
            int fileSize = file.available();
            System.out.println("File size = " + fileSize);
            String shortFileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
            System.out.println(shortFileName);
            ZipEntry zipEntry = new ZipEntry(shortFileName);
            zOut.putNextEntry(zipEntry);
            Reader reader = new Reader(file, queue);
            Writer writer = new Writer(zOut, queue);
            reader.start();
            writer.start();
            reader.join();
            writer.join();
            zOut.closeEntry();
            file.close();
            System.out.println("Ok");
            long finish = System.nanoTime();
            System.out.println("Zipped time: " + (finish - start)/1000000 + " ms");
        }
        zOut.close();
    }

}
