import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.javacv.*;

public class MKVFileMetadata {
    File file;
    String filePath;

    public MKVFileMetadata(String filePath) {
        this.file = new File(filePath);
        this.filePath = file.getAbsolutePath();
    }

    public void DisplayAllMetadata() {
        DisplayFileInfo();
    }

    public void DisplayFileInfo(){
        System.out.println("\n------------------------------------------------------------------------------------\n"
        + "Displaying the file info\n");
        // Getting the file info
        String extension = "";
        extension = FilenameUtils.getExtension(filePath); // Should return mkv

        // Printing for debugging
        System.out.println("File Name: " + file.getName());
        System.out.println("File size: " + file.length());
        System.out.println("File path: " + filePath);
        System.out.println("File extension: " + extension);

        System.out.println( "\nEnd displaying the file info" +
                "\n------------------------------------------------------------------------------------\n");

    }

    public void GrabTheFrame(){
        System.out.println("\n------------------------------------------------------------------------------------\n"
                + "Grabbing the file's framer\n");
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath)){
            grabber.start();
            AVFormatContext FContext = grabber.getFormatContext();
            System.out.println("Total amount of audio streams: " + grabber.getAudioStream());
            // Going through individual streams
            int total_stream = FContext.nb_streams();
            System.out.println("Total number of streams: " + total_stream);
        }catch(Exception e){
            System.out.println("Error while grabbing the file's framer");
        }



        System.out.println( "\nReleased the file's framer" +
                "\n------------------------------------------------------------------------------------\n");
    }

}
