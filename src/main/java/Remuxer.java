import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacv.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Remuxer {
    // Variables need for the remixing
    Path input_file;
    String input_filePath;
    String languageToTarget;
    List<Integer> toKeepStreamIdx;
    Map<Integer,StreamInfo> FileStreamInfo_Map;
    List<String> command;

    // output file properties
    int input_SampleRate;
    int input_VideoCodec;
    int input_AudioCodec;
    int input_VideoBitrate;
    int input_AudioBitrate;
    double input_framerate; // Video framerate
    int input_PixelFormat;
    int input_ImageWidth;
    int input_ImageHeight;
    int input_AudioChannels;
    String input_Format;
    String output_name;


    Remuxer(String input_filePath, String languageToTarget, Map<Integer,StreamInfo> FileStreamInfo_Map) {
        this.command = new ArrayList<>();
        this.input_file = Paths.get(input_filePath);
        this.input_filePath = input_filePath;
        this.languageToTarget = languageToTarget;
        this.toKeepStreamIdx = new ArrayList<>();
        this.FileStreamInfo_Map = new HashMap<>(FileStreamInfo_Map);
        this.output_name = FilenameUtils.getBaseName(input_filePath) + "backup2." + FilenameUtils.getExtension(input_filePath).toLowerCase();
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
            if(streamInfo.getLang().equals(this.languageToTarget)){
                this.toKeepStreamIdx.add(key);
            }
        }

    }

    private void getRemuxingUsingFFmpeg(){

        command.add("ffmpeg ");
        command.add("-i ");
        command.add(this.input_file.toString());
        command.add("-map ");
        command.add("0:v "); // keep all video tracks
        for(int i :  this.toKeepStreamIdx){
            command.add("-map ");
            command.add("-0:a:"+i + " ");
        }
        command.add("-c ");
        command.add("copy ");
        command.add(this.input_file.toString());

    }



    /*
            **Setting up the output file Things we need
                * setFormat
                * setFramerate
                * setSampleRate
                * setVideoCodec
                * setAudioCodec
                * setVideoBitrate
                * setAudioBitrate
                ** Essential needs
                    * imagewidth
                    * imageheight
                    * audiochannels
     */
    private void getInputFileProp(){
        try(FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input_filePath))
        {
            grabber.start();
            input_SampleRate = grabber.getSampleRate();
            input_VideoCodec = grabber.getVideoCodec();
            input_AudioCodec = grabber.getAudioCodec();
            input_VideoBitrate = grabber.getVideoBitrate();
            input_AudioBitrate = grabber.getAudioBitrate();
            input_PixelFormat = grabber.getPixelFormat();
            input_ImageWidth = grabber.getImageWidth();
            input_ImageHeight = grabber.getImageHeight();
            input_AudioChannels = grabber.getAudioChannels();
            input_Format = grabber.getFormat();
            grabber.stop();
        } catch (FrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }
    }


    /*
            - Set the output file
             * params to copy
                - video res, frame rate, format
                - aduio sample rate, channels, and format
                - bitrate
     */

    public void displayImportantContent() {
        System.out.println("------------------------------Printing the Map for the remuxer-----------------------------------------------------------");
        keepSelectedStreamIdx();
        for(int i : this.toKeepStreamIdx){
            System.out.println("Stream Index to keep: " + i);
        }
        getInputFileProp();
        System.out.println("Output file name: " + this.output_name);
        System.out.println("Sameple Rate: " + this.input_SampleRate);
        System.out.println("Video Codec: " + this.input_VideoCodec);
        System.out.println("Audio Codec: " + this.input_AudioCodec);
        System.out.println("Video Bitrate: " + this.input_VideoBitrate);
        System.out.println("Audio Bitrate: " + this.input_AudioBitrate);
        System.out.println("Pixel Format: " + this.input_PixelFormat);
        System.out.println("Image Width: " + this.input_ImageWidth);
        System.out.println("Image Height: " + this.input_ImageHeight);
        System.out.println("Audio Channels: " + this.input_AudioChannels);
        System.out.println("Format: " + this.input_Format);
        System.out.println("Video framerate: " + this.input_framerate);
        getRemuxingUsingFFmpeg();
        for(String s : this.command){
            System.out.println(s);
        }
    }

}
