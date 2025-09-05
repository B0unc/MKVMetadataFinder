import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FolderToFile {
    String directoryPath;
    Path userPath;
    List<Path> files;

    public FolderToFile(String FolderPath) {
        this.directoryPath = FolderPath;
        files = new ArrayList<>();

    }

    private void getFilesFromFolder() throws IOException {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path path : stream) {
                if(FilenameUtils.getExtension(path.getFileName().toString()).equalsIgnoreCase("mkv")) {
                    files.add(path);
                }
            }
        }

    }

    public List<Path> getFiles() throws IOException {
        getFilesFromFolder();
        return files;
    }




}
