import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Remuxer {
    // Variables need for the remixing
    Path file;
    String filePath;
    String languageToTarget;
    List<Integer> toKeepStreamIdx;
    Map<Integer,StreamInfo> FileStreamInfo_Map;

    Remuxer(String filePath, String languageToTarget, Map<Integer,StreamInfo> FileStreamInfo_Map) {
        this.file = Paths.get(filePath);
        this.filePath = filePath;
        this.languageToTarget = languageToTarget;
        this.toKeepStreamIdx = new ArrayList<>();
        this.FileStreamInfo_Map = new HashMap<>(FileStreamInfo_Map);
    }

    /*
        - Get the all the stream index of the tracks you want to keep
            - I need the map from the MKVFileMetadata
                - I could just copy the map from

     */

    private void keepSelectedStreamIdx(){
        for(Map.Entry<Integer,StreamInfo> entry: FileStreamInfo_Map.entrySet()){
            int key = entry.getKey();
            StreamInfo streamInfo = entry.getValue();
            if(streamInfo.getLang().equals(this.languageToTarget) ||
                    streamInfo.getStreamCodecType() == avutil.AVMEDIA_TYPE_VIDEO){
                this.toKeepStreamIdx.add(key);
            }
        }

    }

    public void displayImportantContent(){
        System.out.println("------------------------------Printing the Map for the remuxer-----------------------------------------------------------");
        keepSelectedStreamIdx();
        for(int i : this.toKeepStreamIdx){
            System.out.println("Stream Index to keep: " + i);
        }

    }

}
