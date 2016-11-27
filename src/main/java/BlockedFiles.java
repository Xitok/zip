import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TiM on 27.11.2016.
 */
public class BlockedFiles {

    private static final BlockedFiles instance = new BlockedFiles();
    private List<String> blockZip = new LinkedList<String>();
    private List<String> blockFiles = new LinkedList<String>();

    private BlockedFiles(){

    }

    public static BlockedFiles getInstance(){
        return instance;
    }

    public List<String> getBlockZip() {
        return blockZip;
    }

    public void setBlockZip(List<String> blockZip) {
        this.blockZip = blockZip;
    }

    public List<String> getBlockFiles() {
        return blockFiles;
    }

    public void setBlockFiles(List<String> blockFiles) {
        this.blockFiles = blockFiles;
    }

    public boolean addZipToBlock(String fileName){
        if (fileName == null){
            return false;
        }
        synchronized (blockZip){
            if (!blockZip.contains(fileName)){
                blockZip.add(fileName);
                return true;
            }
        }
        return false;
    }

    public boolean addFilesToBlock(String[] fileNames){
        if (fileNames == null || fileNames.length == 0){
            return false;
        }
        synchronized (blockFiles){
            for (int i = 0; i < fileNames.length; i++){
                if (blockFiles.contains(fileNames[i])){
                    return false;
                }
            }
            for (int i = 0; i < fileNames.length; i++){
                blockFiles.add(fileNames[i]);
            }
        }
        return true;
    }

    public boolean removeZip(String fileName){
        if (fileName == null){
            return false;
        }
        synchronized (blockZip){
            if (blockZip.contains(fileName)){
                return blockZip.remove(fileName);
            }
        }
        return false;
    }

    public boolean removeFiles(String[] fileNames){
        if (fileNames == null || fileNames.length == 0){
            return false;
        }
        synchronized (blockFiles){
            for (int i = 0; i < fileNames.length; i++){
                if (blockFiles.contains(fileNames[i])){
                    blockFiles.remove(fileNames[i]);
                }
            }
            return true;
        }
    }
}
