public class StreamInfo {
    // This should be used as a list in javaFX table viewer
    /*
        For example:
        List<StreamInfo> StreamDes = new ArrayList<>()
        FileDes.add(new StreamInfo(StreamCodecType, StreamIdx, Lang, title));
        FileDes.add(new StreamInfo(avutil.AVMEDIA_TYPE_SUBTITLE, i, SearchThroughStreamMetadata(metadata,"language"), SearchThroughStreamMetadata(metadata,"title")))
     */
     private final int StreamCodecType;
     private final int StreamIdx; // This would be the key for the map

     private final String Lang;
     private final String title;

    public StreamInfo(int StreamCodecType, int StreamIdx, String Lang, String title) {
        this.StreamCodecType = StreamCodecType;
        this.StreamIdx = StreamIdx;
        this.Lang = Lang;
        this.title = title;
    }

    // Get
    public int getStreamCodecType(int streamCodecType){
        return this.StreamCodecType;
    }
    public int getStreamIdx(int streamIdx){
        return this.StreamIdx;
    }
    public String getLang(String lang){
        return this.Lang;
    }
    public String getTitle(String title){
        return this.title;
    }

}
