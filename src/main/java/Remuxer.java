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
    Map<Integer,StreamInfo> remuxFileStreamInfo_Map;
    Map<String, String> mapForSetOptions;

    Remuxer(String filePath, String languageToTarget) {
        this.file = Paths.get(filePath);
        this.filePath = filePath;
        this.languageToTarget = languageToTarget;
        this.remuxFileStreamInfo_Map = new HashMap<>();
        this.mapForSetOptions = new HashMap<>();
        mapForSetOptions.put("-c","copy");
    }


    private void removeTracksRemuxer( Map<Integer,StreamInfo> FileStreamInfo_Map){
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath)){
            grabber.start();
            // construct the output file
            try(FFmpegFrameRecorder outputFile = new FFmpegFrameRecorder(filePath, grabber.getImageWidth(),
                    grabber.getImageHeight(), grabber.getImageHeight())) {
                List<String> BuildStreamMap = new ArrayList<>();
                for (Map.Entry<Integer, StreamInfo> entry : FileStreamInfo_Map.entrySet()) {
                   if(entry.getValue().getStreamCodecType() == avutil.AVMEDIA_TYPE_VIDEO)
                   {
                       System.out.println("Getting the video stream");
                       remuxFileStreamInfo_Map.put(entry.getKey(), entry.getValue());
                       BuildStreamMap.add("0:" + entry.getKey().toString());
                       mapForSetOptions.put("-map","0:" + entry.getKey());
                   }
                   else if ( ((entry.getValue().getStreamCodecType() == avutil.AVMEDIA_TYPE_AUDIO) || (entry.getValue().getStreamCodecType() == avutil.AVMEDIA_TYPE_SUBTITLE)) && entry.getValue().Lang().equals(languageToTarget)) {
                        System.out.println("adding key " + entry.getKey() + " and value " + entry.getValue());
                        remuxFileStreamInfo_Map.put(entry.getKey(), entry.getValue());
                        BuildStreamMap.add("0:" + entry.getKey().toString());
                        mapForSetOptions.put("-map","0:" + entry.getKey());
                        System.out.println("0:" + entry.getKey());
                        System.out.println("added Tracks for " + entry.getKey());
                    }
                }
            } catch (FrameRecorder.Exception e) {
                FFmpegLogCallback.set();
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
            grabber.stop();
            grabber.release();

        } catch (FrameGrabber.Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void printRemuxMap(){
        System.out.println("------------------------------Printing the Map after remux-----------------------------------------------------------");
        remuxFileStreamInfo_Map.forEach((key,value)-> System.out.println(key + " : " + value));
    }

    public void runRemuxer(Map<Integer,StreamInfo> FileStreamInfo_Map){
        removeTracksRemuxer(FileStreamInfo_Map);
        printRemuxMap();
    }

}
