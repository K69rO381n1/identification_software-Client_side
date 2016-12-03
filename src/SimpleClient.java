import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;


public class SimpleClient implements TransmissionProtocol {

    private static final int MAX_TRANSFER_AT_ONCE = 2048;

    private static final String CAPTCHA_IMAGE_PATH = "/temp/captcha";

    private static final byte FALSE = 0;

    private Socket s;
    private OutputStream out;
    private InputStream in;

    public SimpleClient(String host, int port) throws IOException {
        s = new Socket(host, port);
        out = s.getOutputStream();
        in = s.getInputStream();
    }

    private static int roundToLowerPowerOf2(int bufferSize) {
        return 1 << (Integer.bitCount(bufferSize) - 1);
    }

    private byte[] readAndUnwrapPacket() throws IOException {

        int dataSize = TransmissionProtocol.byteArrayToInt(
                receive(TransmissionProtocol.NUM_OF_BYTES_IN_DATA_SIZE));

        return receive(dataSize);
    }

    private byte[] receive(int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int fillIndex = 0;

        while (fillIndex < bufferSize) {
            fillIndex += this.in.read(buffer, fillIndex,
                    Math.min(MAX_TRANSFER_AT_ONCE, roundToLowerPowerOf2(bufferSize - fillIndex)));
        }
        return buffer;
    }

    @Override
    public void getCaptcha() {

        try {
            out.write(TransmissionProtocol.wrapData(TransmissionProtocol.CAPTCHA_REQUEST));

            byte[] data = readAndUnwrapPacket();
            if (data[0] == TransmissionProtocol.CAPTCHA_RESPONSE) {
                TransmissionProtocol.bytes2image(Arrays.copyOfRange(data, 1, data.length), CAPTCHA_IMAGE_PATH);

            } else throw new IOException("Data not in right format");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkCaptchaTextGuess(String userGuess) {
        try {
            out.write(
                    TransmissionProtocol.wrapData(
                            TransmissionProtocol.CAPTCHA_TEXT_CHECK_REQUEST,
                            TransmissionProtocol.fregmentStrings(userGuess)));

            byte[] data = readAndUnwrapPacket();
            if (data[0] == TransmissionProtocol.CAPTCHA_TEXT_CHECK_RESPONSE) {
                return data[1] != FALSE;

            } else throw new IOException("Data not in right format");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkCradentials(String username, String password) {
        try {
            out.write(
                    TransmissionProtocol.wrapData(
                            TransmissionProtocol.CREDENTIALS_CHECK_REQUEST,
                            TransmissionProtocol.fregmentStrings(username, password)));

            byte[] data = readAndUnwrapPacket();
            if (data[0] == TransmissionProtocol.CREDENTIALS_CHECK_RESPONSE) {
                return data[1] != FALSE;

            } else throw new IOException("Data not in right format");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkFaceImage(String faceImagePath) {
        try {
            out.write(
                    TransmissionProtocol.wrapData(
                            TransmissionProtocol.FACE_IMAGE_CHECK_REQUEST,
                            TransmissionProtocol.image2bytes(faceImagePath)));

            byte[] data = readAndUnwrapPacket();
            if (data[0] == TransmissionProtocol.FACE_IMAGE_CHECK_RESPONSE) {
                return data[1] != FALSE;

            } else throw new IOException("Data not in right format");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object getStatistics() {
        return null;
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        try {
            out.write(
                    TransmissionProtocol.wrapData(
                            TransmissionProtocol.CHANGE_PASSWORD_REQUEST,
                            TransmissionProtocol.fregmentStrings(username, oldPassword, newPassword)));

            byte[] data = readAndUnwrapPacket();
            if (data[0] == TransmissionProtocol.CHANGE_PASSWORD_RESPONSE) {
                return data[1] != FALSE;

            } else throw new IOException("Data not in right format");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addImage(String username, String password, String imagePath) {
        try {
            out.write(
                    TransmissionProtocol.wrapData(
                            TransmissionProtocol.ADD_IMAGE_REQUEST,
                            Utils.concatenate(
                                    TransmissionProtocol.fregmentStrings(username, password),
                                    TransmissionProtocol.image2bytes(imagePath))));

            byte[] data = readAndUnwrapPacket();
            if (data[0] == TransmissionProtocol.ADD_IMAGE_RESPONSE) {
                return data[1] != FALSE;

            } else throw new IOException("Data not in right format");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //*********************************************** method's Helpers **********************************************

    public void close() throws IOException {
        in.close();
        out.close();
        s.close();
    }
}
