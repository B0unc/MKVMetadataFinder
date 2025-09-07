import org.bytedeco.ffmpeg.global.avutil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMKVFileMetadata {


    @Test
    @DisplayName(" Test the Map file to see if it works ")
    void TestFileStreamInfoMap(){
        // Test Setup
        Map<Integer,StreamInfo> TestFileStreamMap = new HashMap<>();
        /* I prefer you put the values in manually using ffmpeg as a references
        */
        TestFileStreamMap.put(1, new StreamInfo(avutil.AVMEDIA_TYPE_AUDIO, "jpn", null));
        TestFileStreamMap.put(2, new StreamInfo(avutil.AVMEDIA_TYPE_SUBTITLE, "eng", null));
        TestFileStreamMap.forEach((k,v)-> System.out.println(k+" : "+ v.getStreamCodecType() + ", " + v.getLang() + ", " + v.title()));

        // Function Setup
        MKVFileMetadata file = new MKVFileMetadata("C:\\Users\\Thomas\\MKVMetadataFinder\\src\\test\\java\\testfile1.mkv");
        file.GrabTheFrame();
        Map<Integer,StreamInfo> RealFileStreamMap = file.getFileStreamInfo_Map();
        file.printFileStreamInfo_map(); // Set the printFileStreamInfo_map in the MKVFileMetadata to public so you can also see the map content of the real thing

        Assertions.assertEquals(TestFileStreamMap.equals(RealFileStreamMap), RealFileStreamMap.equals(TestFileStreamMap));

    }

    @Test
    @DisplayName(" Test the Directory")
    void TestDirectory(){
        List<Path> TestDirectoryList = new ArrayList<>();
        Path TestPath = Paths.get("C:\\Users\\Thomas\\MKVMetadataFinder\\src\\test\\java\\Food Court\\test02.mkv");
        TestDirectoryList.add(TestPath);
        TestPath = Paths.get("C:\\Users\\Thomas\\MKVMetadataFinder\\src\\test\\java\\Food Court\\test 03.mkv");
        TestDirectoryList.add(TestPath);
        TestPath = Paths.get("C:\\Users\\Thomas\\MKVMetadataFinder\\src\\test\\java\\Food Court\\test 04.mkv");
        TestDirectoryList.add(TestPath);
        TestPath = Paths.get("C:\\Users\\Thomas\\MKVMetadataFinder\\src\\test\\java\\Food Court\\test 05.mkv");
        TestDirectoryList.add(TestPath);

    }

}
