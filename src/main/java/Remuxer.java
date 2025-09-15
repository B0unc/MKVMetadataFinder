import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Remuxer {
    // Variables need for the remixing
    Path file;
    String filePath;
    String languageToTarget;
    Map<Integer,StreamInfo> remuxFileStreamInfo_Map;


    Remuxer(String filePath, String languageToTarget) {
        this.file = Paths.get(filePath);
        this.filePath = filePath;
        this.languageToTarget = languageToTarget;
        this.remuxFileStreamInfo_Map = new HashMap<>();
    }


    private void removeTracksRemuxer( Map<Integer,StreamInfo> FileStreamInfo_Map){
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath)){
            grabber.start();
            // construct the output file
            try(FFmpegFrameRecorder outputFile = new FFmpegFrameRecorder(filePath, grabber.getImageWidth(),
                    grabber.getImageHeight(), grabber.getImageHeight())) {

                for (Map.Entry<Integer, StreamInfo> entry : FileStreamInfo_Map.entrySet()) {
                    if (entry.getValue().Lang().equals(languageToTarget)) {
                        System.out.println("adding key " + entry.getKey() + " and value " + entry.getValue());
                        remuxFileStreamInfo_Map.put(entry.getKey(), entry.getValue());
                        System.out.println("added Tracks for " + entry.getKey());
                    }
                }
            } catch (FrameRecorder.Exception e) {
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
