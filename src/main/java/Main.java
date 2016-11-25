import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by DA on 24.11.2016.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;
        while (!exit){
            String command = reader.readLine();
            if (command.equals("q")){
                exit = true;
            }
            if (command.startsWith("--zip")){
                String[] commands = command.split("--");
                String zipName = commands[2];
                String[] files = commands[3].split("\\*");
                new ZipFile(zipName, files).zip();
            } else if (command.startsWith("--unzip")){
                String[] commands = command.split("--");
                new UnZipFile(commands[2].trim()).unZip();
            }
        }
    }

}
