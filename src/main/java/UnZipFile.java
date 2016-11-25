import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by DA on 24.11.2016.
 */
public class UnZipFile {

    ZipInputStream zip;
    FileOutputStream file;
    BufferedReader reader;
    ZipEntry entry;
    String fileName;

    public UnZipFile(String fileName, String outputFileName) throws FileNotFoundException {
        System.out.println(fileName);
        System.out.println(outputFileName);
        this.fileName = fileName;
        this.zip = new ZipInputStream(new FileInputStream(fileName));
        this.file = new FileOutputStream(outputFileName);
    }

    public UnZipFile(String fileName) throws FileNotFoundException {
        System.out.println(fileName);
        this.fileName = fileName;
        this.zip = new ZipInputStream(new FileInputStream(fileName));
    }

    public void unZip() throws IOException {
        System.out.println("Unzip " + fileName);
        entry = zip.getNextEntry();
        while (entry != null){
            long start = System.nanoTime();
            System.out.println("File: " + entry.getName() + " " + entry.getSize());
            String outputFile = fileName.substring(0, fileName.lastIndexOf("\\") + 1);
            System.out.println(outputFile);
            file = new FileOutputStream(outputFile + entry.getName());
            int len;
            byte[] bytes = new byte[2048];
            while ((len = zip.read(bytes)) > 0){
                file.write(bytes, 0, len);
            }
            zip.closeEntry();
            file.flush();
            file.close();
            entry = zip.getNextEntry();
            long finish = System.nanoTime();
            System.out.println("Unzipped time: " + (finish - start)/1000000 + " ms");
        }
        zip.close();
        System.out.println("Unzip complited");
    }

}
