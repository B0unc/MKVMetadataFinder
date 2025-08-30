import java.io.File;

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

    public MKVFileMetadata(String filePath) {
        this.file = new File(filePath);
        this.filePath = file.getAbsolutePath();
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
            System.out.println("\nTotal amount of audio streams: " + grabber.getAudioStream());
            System.out.println("Total number of streams: " + FContext.nb_streams());
            // Go through each stream
            for(int i = 0; i < FContext.nb_streams(); i++){

                AVStream stream = FContext.streams(i);
                // System.out.println("Codec ID: " + codecType);
                // Each stream will contain a metadata that we read from and each stream will contain a codec type
                AVDictionary metadata = stream.metadata();
                switch (stream.codecpar().codec_type()){
                    case avutil.AVMEDIA_TYPE_SUBTITLE:
                        System.out.println("---------------------------------- Subtitle Codec Type ----------------------------------------");
                        System.out.println("Subtitle Lang: " + SearchThroughStreamMetadata(metadata,"language"));
                        System.out.println("Subtitle title: " + SearchThroughStreamMetadata(metadata,"title"));
                        break;
                   case avutil.AVMEDIA_TYPE_AUDIO:
                       System.out.println("---------------------------------- Audio Codec Type ----------------------------------------");
                       System.out.println("Audio Lang: " + SearchThroughStreamMetadata(metadata,"language"));
                       System.out.println("Audio title: " + SearchThroughStreamMetadata(metadata,"title"));
                       break;
                   case avutil.AVMEDIA_TYPE_VIDEO:
                       System.out.println("Video Codec Type");
                       break;
                }
            }
            System.out.println("----------------------------------------------------------------------------------------------------");


        }catch(Exception e){
            System.out.println("Error while grabbing the file's framer");
        }

        System.out.println("""
                
                Released the file's framer\
                
                ------------------------------------------------------------------------------------
                """);
    }

    private String SearchThroughStreamMetadata(AVDictionary metadata, String s){
        AVDictionaryEntry entry = null;
        entry = avutil.av_dict_get(metadata, s, entry, avutil.AV_DICT_IGNORE_SUFFIX);
        if(entry == null){
            return null;
        }
        return entry.value().getString();
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

}
