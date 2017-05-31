package in.visheshagya.visheshagya.getterSetter;

/**
 * Created by VISHESHAGYA on 10/14/2016.
 */

public class ElockerFolders {

    public String folderName;
    public String fileName;

    public ElockerFolders(String folderName) {
        this.folderName = folderName;
        this.fileName = folderName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
