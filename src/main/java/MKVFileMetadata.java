import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.io.FilenameUtils;
import org.bytedeco.ffmpeg.avcodec.AVCodecParameters;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.avutil.AVDictionaryEntry;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

public class MKVFileMetadata {
    Path file;
    String filePath;
    Map<Integer,StreamInfo> FileStreamInfo_Map;
    Map<Integer,inputStreamCodec> inputStreamCodec_map;
    int StreamIdx;

    public MKVFileMetadata(String filePath) {
        this.file = Paths.get(filePath);
        this.filePath = filePath;
        this.FileStreamInfo_Map = new HashMap<>();
        inputStreamCodec_map = new HashMap<>();
        this.StreamIdx = -1;
    }

    public void DisplayAllMetadata() {
        DisplayFileInfo();
        GrabTheFrame();
        printFileStreamInfo_map();
        Remuxer fileToRemux = new Remuxer(filePath, "jpn");
        fileToRemux.runRemuxer(FileStreamInfo_Map);
    }


    public void DisplayFileInfo(){
        System.out.println("""
                
                ------------------------------------------------------------------------------------
                Displaying the file info
                """);
        String extension = FilenameUtils.getExtension(filePath);

        System.out.println("File Name: " + file.toFile().getName());
        System.out.println("File size: " + file.toFile().length());
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
                AVCodecParameters in_codecpar = stream.codecpar();

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
                       FileStreamInfo_Map.put(i,new StreamInfo(avutil.AVMEDIA_TYPE_VIDEO, null, null));
                       System.out.println(stream.codecpar().codec_type());
                       break;
                }
                inputStreamCodec_map.put(i, new inputStreamCodec(in_codecpar)); // just grab everything for now
            }
            grabber.stop();
            grabber.release();


        }catch(Exception e){
            System.out.println("Error while grabbing the file's framer");
        }

    }

    private String SearchThroughStreamMetadata(AVDictionary metadata, String s){
        AVDictionaryEntry entry = null;
        entry = avutil.av_dict_get(metadata, s, entry, avutil.AV_DICT_IGNORE_SUFFIX);
        // Optionals are sick
        return Optional.ofNullable(entry).map(e -> e.value().getString()).orElse(null);
    }


    // Get the List for the File Stream
    public Map<Integer,StreamInfo> getFileStreamInfo_Map(){
        return FileStreamInfo_Map;
    }
/*
    public Path getFile(){
        return file;
    }
 */

    public void printFileStreamInfo_map(){
        System.out.println("------------------------------Printing the Map-----------------------------------------------------------");
        FileStreamInfo_Map.forEach((k,v)-> System.out.println(k+" : "+ v.getStreamCodecType() + ", " + v.getLang() + ", " + v.title()));
        // inputStreamCodec_map.forEach((k,v) -> System.out.println(k + ": " + v.getParameters()));
    }


}
