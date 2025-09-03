import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TestMKVFileMetadata {

    @Test
    @DisplayName(" Test the Map file to see if it works ")
    void TestFileStreamInfoMap(){
        Map<Integer,StreamInfo> TestFileStreamMap = new HashMap<Integer, StreamInfo>();
        MKVFileMetadata file = new MKVFileMetadata("testfile1.mkv");
        /*
            testfile1.mkv should follow as
            Key:        Value: (StreamCodecType, Lang, Title)
            1              AVMEDIA_TYPE_AUDIO, JPN, NULL
            2              AVMEDIA_TYPE_SUBTITLE, ENG, NULL
        */
        TestFileStreamMap.put(1, new StreamInfo(avutil.AVMEDIA_TYPE_AUDIO, "jpn", null));
        TestFileStreamMap.put(2, new StreamInfo(avutil.AVMEDIA_TYPE_SUBTITLE, "eng", null));
        TestFileStreamMap.forEach((k,v)-> System.out.println(k+" : "+ v.getStreamCodecType() + ", " + v.getLang() + ", " + v.getTitle()));

    }

}
