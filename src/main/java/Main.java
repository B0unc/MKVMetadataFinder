
public class Main {

    public static void main(String[] args){
        String path = "C:\\Users\\Thomas\\MKVMetadataFinder\\src\\main\\java\\test.mkv";
        MKVFileMetadata file = new MKVFileMetadata(path);
        System.out.println("Start testing");
        file.DisplayAllMetadata();
        System.out.println("End testing");
    }
}
