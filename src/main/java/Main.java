import backupFiles.backupUserFile;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        // FolderToFile userDirectoryFolder = new FolderToFile("src/main/java/n2");
        // List<Path> userFiles = userDirectoryFolder.getFiles();
        /*
        for(Path userFile : userFiles) {
            String path = userFile.toString();
            String fileName = userFile.getFileName().toString();
            MKVFileMetadata file = new MKVFileMetadata(path);
            System.out.println("Start testing for " + fileName);
            file.DisplayAllMetadata();
            System.out.println("End testing for " + fileName);
        }
         */
/*
        MKVFileMetadata userMKVMetadataExtractor = new MKVFileMetadata("src/main/java/test.mkv");
        userMKVMetadataExtractor.DisplayAllMetadata();
    */
        backupUserFile test = new backupUserFile("src/main/java/test.mkv");
        test.DisplayInfoForBackup();
        test.backupTheUserFile();
    }
}
