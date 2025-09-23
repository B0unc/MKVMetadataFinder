import backupFiles.backupUserFile;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        backupUserFile test = new backupUserFile("src/main/java/test.mkv");
        test.DisplayInfoForBackup();
        test.backupTheUserFile();
        MKVFileMetadata test_1 = new MKVFileMetadata("src/main/java/test.mkv");
        test_1.DisplayAllMetadata();
    }
}
