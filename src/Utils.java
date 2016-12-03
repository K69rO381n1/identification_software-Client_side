
public class Utils {

    public static byte[] concatenate(byte[]... byteArrays) {
        int totalLength = 0;
        for (byte[] byteArray : byteArrays) totalLength += byteArray.length;
        byte[] concatenatedArray = new byte[totalLength];
        int i = 0;
        for (byte[] byteArray : byteArrays) {
            System.arraycopy(byteArray, 0, concatenatedArray, i, byteArray.length);
            i += byteArray.length;
        }
        return concatenatedArray;
    }
}
