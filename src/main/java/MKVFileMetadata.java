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
    Map<Integer,StreamInfo> FileStreamInfo_Map;
    int StreamIdx;

    public MKVFileMetadata(String filePath) {
        this.file = new File(filePath);
        this.filePath = file.getAbsolutePath();
        this.FileStreamInfo_Map = new HashMap<>();
        this.StreamIdx = -1;
    }

    public void DisplayAllMetadata() {
        DisplayFileInfo();
        GrabTheFrame();
        printFileStreamInfo_map();
    }


    public void DisplayFileInfo(){
        System.out.println("""
                
                ------------------------------------------------------------------------------------
                Displaying the file info
                """);
        String extension = FilenameUtils.getExtension(filePath);

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

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath)){
            grabber.start();
            AVFormatContext FContext = grabber.getFormatContext();

            String Lang;
            String title;
            for(int i = 0; i < FContext.nb_streams(); i++){

                AVStream stream = FContext.streams(i);

                AVDictionary metadata = stream.metadata();
                Lang = SearchThroughStreamMetadata(metadata,"language");
                title = SearchThroughStreamMetadata(metadata,"title");
                switch (stream.codecpar().codec_type()){
                    case avutil.AVMEDIA_TYPE_SUBTITLE:
                        FileStreamInfo_Map.put(i,new StreamInfo(avutil.AVMEDIA_TYPE_SUBTITLE, Lang, title));
                        break;
                   case avutil.AVMEDIA_TYPE_AUDIO:
                       FileStreamInfo_Map.put(i,new StreamInfo(avutil.AVMEDIA_TYPE_AUDIO, Lang, null));
                       break;
                   case avutil.AVMEDIA_TYPE_VIDEO:
                       break;
                }
            }


        }catch(Exception e){
            System.out.println("Error while grabbing the file's framer");
        }

    }

    private String SearchThroughStreamMetadata(AVDictionary metadata, String s){
        AVDictionaryEntry entry = null;
        entry = avutil.av_dict_get(metadata, s, entry, avutil.AV_DICT_IGNORE_SUFFIX);
        // Optional is sick
        return Optional.ofNullable(entry).map(e -> e.value().getString()).orElse(null);
    }


    // Get the List for the File Stream
    public Map<Integer,StreamInfo> getFileStreamInfo_Map(){
        return FileStreamInfo_Map;
    }

    public void printFileStreamInfo_map(){
        System.out.println("------------------------------Printing the Map-----------------------------------------------------------");
        FileStreamInfo_Map.forEach((k,v)-> System.out.println(k+" : "+ v.getStreamCodecType() + ", " + v.getLang() + ", " + v.getTitle()));
    }


}
