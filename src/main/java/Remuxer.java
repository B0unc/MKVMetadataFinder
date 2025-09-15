import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;

public class Remuxer {
    // Variables need for the remixing
    AVPacket pkt;
    AVFormatContext inputFormatContext;
    AVFormatContext outputFormatContext;


    Remuxer(AVFormatContext inputFormatContext) {
        this.inputFormatContext = inputFormatContext;
        this.outputFormatContext = null;
        this.pkt = null;
    }


}
