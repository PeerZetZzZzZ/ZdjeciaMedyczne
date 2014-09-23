package model.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import model.ResourceBundleMaster;
import model.exception.FileTooBigException;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author PeerZet
 */
public class FileDeliver {

    List<File> fileList = new ArrayList<File>();
    HashMap<String, String> fileMap = new HashMap<String, String>();
    List<String> extensionList;

    public FileDeliver(List<File> file) throws FileNotFoundException, FileTooBigException, IOException {
        this.extensionList = new ArrayList<String>(Arrays.asList("jpg", "jpeg", "bmp"));
        fileList = file;
        getDataFromFiles(fileList);
    }

    public FileDeliver() {
        this.extensionList = new ArrayList<String>(Arrays.asList("jpg", "jpeg", "bmp"));
    }

    private void getDataFromFiles(List<File> fileList) throws FileNotFoundException, FileTooBigException, IOException {
        for (File file : fileList) {
            if (verifyFileExtension(file.getAbsolutePath())) {
                long fileLength = file.length();
                if (fileLength > (1024 * 1024 * 10)) {
                    throw new FileTooBigException(ResourceBundleMaster.TRANSLATOR.getTranslation("fileTooBig"));
                }//we dont have to read file as byte array
                fileMap.put(file.getName(), file.getAbsolutePath());
            }
        }
    }

    public boolean verifyFileExtension(String filePath) {
        String extension = FilenameUtils.getExtension(filePath);
        for (String allowedExtension : extensionList) {
            if (extension.equals(allowedExtension)) {
                return true;//if it's allowed extension 
            }
        }
        return false;

    }

    public HashMap<String, String> getFilesData() {
        return fileMap;
    }

    public void saveFile(String filePath, byte[] data) throws FileNotFoundException, IOException {
        FileOutputStream fileStream = new FileOutputStream(filePath);
        fileStream.write(data);
        fileStream.close();
    }
}
