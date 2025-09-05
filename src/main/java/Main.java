import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Path> userFiles;
        FolderToFile userDirectoryFolder = new FolderToFile("C:\\Users\\Thomas\\MKVMetadataFinder\\src\\main\\java\\Food Court\\");
        userFiles = userDirectoryFolder.getFiles();
        for(Path userFile : userFiles) {
            String path = userFile.toString();
            String fileName = userFile.getFileName().toString();
            MKVFileMetadata file = new MKVFileMetadata(path);
            System.out.println("Start testing for " + fileName);
            file.DisplayAllMetadata();
            System.out.println("End testing for " + fileName);
        }
    }
}
