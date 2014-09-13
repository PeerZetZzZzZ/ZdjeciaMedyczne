package model.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import model.ResourceBundleMaster;
import model.exception.FileTooBigException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author PeerZet
 */
public class FileDeliver {

    List<File> fileList = new ArrayList<File>();
    HashMap<String, byte[]> fileMap = new HashMap<String, byte[]>();
    List<String> extensionList;

    public FileDeliver(List<File> file) throws FileNotFoundException, FileTooBigException, IOException {
        this.extensionList = new ArrayList<String>(Arrays.asList("jpg", "jpeg", "bmp"));
        fileList = file;
        getDataFromFiles(fileList);
    }

    private void getDataFromFiles(List<File> fileList) throws FileNotFoundException, FileTooBigException, IOException {
        for (File file : fileList) {
            if (verifyFilExtension(file.getAbsolutePath())) {
                long fileLength = file.length();
                if (fileLength > (1024 * 1024 * 10)) {
                    throw new FileTooBigException(ResourceBundleMaster.TRANSLATOR.getTranslation("fileTooBig"));
                }
                int length = (int) fileLength;
                byte[] fileArray = new byte[length];
                fileArray = FileUtils.readFileToByteArray(file);
                fileMap.put(file.getName(), fileArray);
            }
        }
    }

    private boolean verifyFilExtension(String filePath) {
        String extension = FilenameUtils.getExtension(filePath);
        for (String allowedExtension : extensionList) {
            if (extension.equals(allowedExtension)) {
                return true;//if it's allowed extension 
            }
        }
        return false;

    }

    public HashMap<String, byte[]> getFilesData() {
        return fileMap;
    }
}
