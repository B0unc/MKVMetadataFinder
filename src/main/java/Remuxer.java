import org.apache.commons.io.FilenameUtils;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Remuxer {
    // Variables need for the remixing
    Path input_file;
    String input_filePath;
    String languageToTarget;
    List<Integer> toRemoveStreamIdx;
    List<String> command;

    String output_name;


    Remuxer(String input_filePath, String languageToTarget) {
        this.command = new ArrayList<>();
        this.input_file = Paths.get(input_filePath);
        this.input_filePath = input_filePath;
        this.languageToTarget = languageToTarget;
        this.toRemoveStreamIdx = new ArrayList<>();
        this.output_name = FilenameUtils.getBaseName(input_filePath) + "backup2." +
                FilenameUtils.getExtension(input_filePath).toLowerCase();
    }

    /*
        - Get the all the stream index of the tracks you want to keep
            - I need the map from the MKVFileMetadata
                - I could just copy the map from

     */
    private void keepSelectedStreamIdx(Map<Integer,StreamInfo> FileStreamInfo_Map){
        for(Map.Entry<Integer,StreamInfo> entry: FileStreamInfo_Map.entrySet()){
            int key = entry.getKey();
            StreamInfo streamInfo = entry.getValue();
            if(!streamInfo.getLang().equals(this.languageToTarget) &&
                    streamInfo.getStreamCodecType() != avutil.AVMEDIA_TYPE_VIDEO){
                this.toRemoveStreamIdx.add(key);
            }
        }

    }

    private void getRemuxingUsingFFmpeg() throws IOException {

        command.add("ffmpeg ");
        command.add("-i");
        command.add(this.input_file.toString());
        command.add("-map");
        command.add("0"); // Keeps the video DO NOT REMOVE
        for(int i :  this.toRemoveStreamIdx){
            command.add("-map");
            command.add("-0:"+i);
        }
        command.add("-c");
        command.add("copy");
        command.add(this.output_name);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
            }
            int exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("done");
    }

    public void runRemuxer(Map<Integer,StreamInfo> FileStreamInfo_Map) throws IOException {
        keepSelectedStreamIdx(FileStreamInfo_Map);
        getRemuxingUsingFFmpeg();
    }

}
