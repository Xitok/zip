import java.io.*;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.zip.*;
import java.util.zip.ZipFile;

/**
 * Created by DA on 24.11.2016.
 */
public class UnZipFile extends Thread {

    ZipInputStream zip;
    FileOutputStream file;
    ZipEntry entry;
    String fileName;


    public UnZipFile(String fileName) throws FileNotFoundException {
        System.out.println(fileName);
        this.fileName = fileName;

    }

    @Override
    public void run() {
        BlockedFiles bf = BlockedFiles.getInstance();
        if (bf.addZipToBlock(fileName)){
            try {
                this.zip = new ZipInputStream(new FileInputStream(fileName));
                unZip();
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("UnZip error");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bf.removeZip(fileName);
            }
            System.out.println("UnZip complete");
        }
    }

    public void unZip() throws IOException, InterruptedException {
        System.out.println("Unzip " + fileName);
        java.util.zip.ZipFile zipFile = new ZipFile(fileName);
        try {
            for (Enumeration<? extends ZipEntry> e = zipFile.entries();
                 e.hasMoreElements();){
                entry = e.nextElement();
                ArrayBlockingQueue<byte[]> queue = new ArrayBlockingQueue<byte[]>(10);
                long start = System.nanoTime();
                System.out.println("File: " + entry.getName() + " " + entry.getSize());
                String outputFile = fileName.substring(0, fileName.lastIndexOf("\\") + 1);
                System.out.println(outputFile);
                file = new FileOutputStream(outputFile + entry.getName());
                Reader reader = new Reader(zipFile.getInputStream(entry), queue);
                Writer writer = new Writer(file, queue);
                reader.start();
                writer.start();
                reader.join();
                writer.join();
                zipFile.getInputStream(entry).close();
                file.flush();
                file.close();
                entry = zipFile.entries().nextElement();
                long finish = System.nanoTime();
                System.out.println("Unzipped time: " + (finish - start)/1000000 + " ms");
            }
        } catch (NoSuchElementException e) {
            System.out.println("No more entries");
        } finally {
            zipFile.close();
        }
        System.out.println("Unzip complited");
    }

}
