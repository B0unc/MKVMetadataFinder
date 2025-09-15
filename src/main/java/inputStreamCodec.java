import org.bytedeco.ffmpeg.avcodec.AVCodecParameters;

public record inputStreamCodec(AVCodecParameters in_codecpar) {
    public AVCodecParameters getParameters() {
        return this.in_codecpar;
    }
}
