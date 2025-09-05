/**
 * @param StreamCodecType This should be used as a list in javaFX table viewer
 *                        For example:
 *                        List<StreamInfo> StreamDes = new ArrayList<>()
 *                        FileDes.add(new StreamInfo(StreamCodecType, StreamIdx, Lang, title));
 *                        FileDes.add(new StreamInfo(avutil.AVMEDIA_TYPE_SUBTITLE, i, SearchThroughStreamMetadata(metadata,"language"), SearchThroughStreamMetadata(metadata,"title")))
 */
public record StreamInfo(int StreamCodecType, String Lang, String title) {

    // Get
    public int getStreamCodecType() {
        return this.StreamCodecType;
    }

    public String getLang() {
        return this.Lang;
    }

}
