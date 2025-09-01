import java.io.File;
import java.util.*;

import org.apache.commons.io.FilenameUtils;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.avutil.AVDictionaryEntry;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

public class MKVFileMetadata {
    File file;
    String filePath;
    Map<Integer,StreamInfo> FileStreamInfo;
    int StreamIdx;

    public MKVFileMetadata(String filePath) {
        this.file = new File(filePath);
        this.filePath = file.getAbsolutePath();
        this.FileStreamInfo = new HashMap<>();
        this.StreamIdx = -1;
    }

    public void DisplayAllMetadata() {
        DisplayFileInfo();
        GrabTheFrame();
    }

    public void DisplayFileInfo(){
        System.out.println("""
                
                ------------------------------------------------------------------------------------
                Displaying the file info
                """);
        // Getting the file info
        String extension = FilenameUtils.getExtension(filePath); // Should return mkv

        // Printing for debugging
        System.out.println("File Name: " + file.getName());
        System.out.println("File size: " + file.length());
        System.out.println("File path: " + filePath);
        System.out.println("File extension: " + extension);

        System.out.println("""
                
                End displaying the file info\
                
                ------------------------------------------------------------------------------------
                """);

    }

    /*
        Para: none
        Return type: none
        Description: This is going to be long
            - Grab the frame and start it so we can the format context
            - From the Format context we can get the most valuable one which is the streams
                - Streams as I can understand it contain the all the metadata that we need (I know im wrong but still)
            - From each stream we can get the important information audio and subtitle tracks (I also got the video tracks for later (I feel like it))
            - We can print the information that we need
                - I think a map will be a good data structure for this
     */
    public void GrabTheFrame(){
        System.out.println("""
                
                ------------------------------------------------------------------------------------
                Grabbing the file's framer
                """);

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath)){
            grabber.start();
            // Need this for the streams
            AVFormatContext FContext = grabber.getFormatContext();
            System.out.println("\n--------------------------------------------------------------------------------------------");
            System.out.println("Total amount of audio streams: " + grabber.getAudioStream());
            System.out.println("Total number of streams: " + FContext.nb_streams());
            // Go through each stream
            for(int i = 0; i < FContext.nb_streams(); i++){

                AVStream stream = FContext.streams(i);
                // System.out.println("Codec ID: " + codecType);
                // Each stream will contain a metadata that we read from and each stream will contain a codec type
                AVDictionary metadata = stream.metadata();
                switch (stream.codecpar().codec_type()){
                    case avutil.AVMEDIA_TYPE_SUBTITLE:
                        System.out.println("---------------------------------- Subtitle Codec Type -------------------------------------");
                        System.out.println("Stream Index: " + i);
                        System.out.println("Subtitle Lang: " + SearchThroughStreamMetadata(metadata,"language"));
                        System.out.println("Subtitle title: " + SearchThroughStreamMetadata(metadata,"title"));
                        break;
                   case avutil.AVMEDIA_TYPE_AUDIO:
                       System.out.println("---------------------------------- Audio Codec Type ----------------------------------------");
                       System.out.println("Stream Index: " + i);
                       System.out.println("Audio Lang: " + SearchThroughStreamMetadata(metadata,"language"));
                       System.out.println("Audio title: " + SearchThroughStreamMetadata(metadata,"title"));
                       break;
                   case avutil.AVMEDIA_TYPE_VIDEO:
                       System.out.println("---------------------------------- Video Codec Type ----------------------------------------");
                       System.out.println("Stream Index: " + i);
                       System.out.println("Video Codec Type");
                       PrintAllMetadata(metadata);
                       break;
                }
            }


        }catch(Exception e){
            System.out.println("Error while grabbing the file's framer");
        }
        System.out.println("--------------------------------------------------------------------------------------------");

        System.out.println("""
                
                
                ------------------------------------------------------------------------------------
                Released the file's framer\
                """);
    }

    private String SearchThroughStreamMetadata(AVDictionary metadata, String s){
        AVDictionaryEntry entry = null;
        entry = avutil.av_dict_get(metadata, s, entry, avutil.AV_DICT_IGNORE_SUFFIX);
        // Optional is sick
        return Optional.ofNullable(entry).map(e -> e.value().getString()).orElse(null);
    }

    // Thinking about storing this into a file
    private void PrintAllMetadata(AVDictionary metadata){
        AVDictionaryEntry entry = null;
        while((entry = avutil.av_dict_get(metadata, "", entry, avutil.AV_DICT_IGNORE_SUFFIX)) != null){
            String key = entry.key().getString();
            String value = entry.value().getString();
            System.out.println("    " + key + ": " + value);
        }
    }

    // Get the List for the File Stream
    public Map<Integer,StreamInfo> getFileStreamInfo(){
        return FileStreamInfo;
    }

    /*
        Future Plan: I think
            - We want to store all of our File Stream Info into a list and that will contain the following:
                - Subtitle
                    - Stream ID
                    - Lang
                    - Title
                - Audio
                    - Stream ID
                    - Lang
                    - Title
         ** We get this information of all files until we are done,
         ** We need to display all of this information to the screen
         --------------------------------Final Super ultra Final-------------------------------------------
            - I think making a map with the stream Index as its key would be the better option
                - Each stream index is unique and the key has to be unique also
            - Now I think for the bigger project scope Maybe have the file name as its key and the map as the value
                - I know it kinda stupid but let's see
     */

}
