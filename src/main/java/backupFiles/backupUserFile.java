package backupFiles;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class backupUserFile {
    Path original_file;
    String original_fileName;
    Path file;
    String fileName;
    String original_extension;
    String original_filePath;
    String backupFileName;
    String backupFilePath;

    public backupUserFile(String filePath) {
        Path file = Paths.get(filePath);
        this.original_filePath = filePath;
        this.original_file = file;
        this.original_fileName = FilenameUtils.removeExtension(file.getFileName().toString());
        this.original_extension = FilenameUtils.getExtension(filePath);
        this.fileName = "";
        this.backupFileName = original_fileName + "backup." + original_extension;
        this.backupFilePath = original_file.getParent().toString() + "/"  + this.backupFileName;
    }

    public void DisplayInfoForBackup(){
        System.out.println("Original file name: " + this.original_fileName);
        System.out.println("Original file extension: " + this.original_extension);
        System.out.println("Backup file name: " + this.backupFileName);
        System.out.println("Original file path: " + original_filePath);
        System.out.println("Backup file path: " + backupFilePath);
    }

    public void backupTheUserFile(){
        Path sourceUserFile = Paths.get(original_filePath);
        Path destinationBackupUserFile = Paths.get(original_file.getParent().toString() + "/" + backupFileName);
        try{
            Files.copy(sourceUserFile, destinationBackupUserFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getBackupFile() {
        return file;
    }
    public String getBackupFilePath() {
        return backupFilePath;
    }
    public String getBackupFileName() {
        return fileName;
    }
}