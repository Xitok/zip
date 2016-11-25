import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by TiM on 24.11.2016.
 */
public class ZipFile {

    private ZipOutputStream zOut;
    private FileInputStream file;
    private String[] files;

    public ZipFile(String zipName, String[] files) throws FileNotFoundException {
        System.out.println("Zip name: " + zipName);
        System.out.println("Files to zip:");
        for (String fileName: files){
            System.out.println(fileName);
        }
        this.zOut = new ZipOutputStream(new FileOutputStream(zipName));
        this.files = files;
    }

    public void zip() throws IOException {
        System.out.println("Zip start");
        for (String fileName: files){
            long start = System.nanoTime();
            file = new FileInputStream(fileName);
            int fileSize = file.available();
            System.out.println("File size = " + fileSize);
            String shortFileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
            System.out.println(shortFileName);
            ZipEntry zipEntry = new ZipEntry(shortFileName);
            zipEntry.setSize(file.available());
            zOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[2048];
            int readed;

            while ((readed = file.read(bytes)) > 0){
                zOut.write(bytes);
                    System.out.println(fileSize-=readed);

            }
            zOut.closeEntry();
            file.close();
            System.out.println("Ok");
            long finish = System.nanoTime();
            System.out.println("Zipped time: " + (finish - start)/1000000 + " ms");
        }
        zOut.close();
        System.out.println("Zip complete");
    }

}
