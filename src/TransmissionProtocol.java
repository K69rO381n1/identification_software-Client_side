import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Send / Receive protocol:
 * Every data will be transferred in its *full form,
 * wrapped with header that contains 3 bytes for that data length and 1 byte for the msg purpose.
 * [length (3 bytes) : Flag (1 byte) : data]
 * <p>
 * Length: integer from 0 to 256^3 (more that 16 MB...)
 * Flag: One of the following values,
 * <p>
 * From client:
 * <p>
 * b'0':   Request for captcha image.
 * Data expected: None
 * <p>
 * b'1':   Request for captcha-text guess check
 * Data expected: [guess length (1 byte) : users's guess...]
 * <p>
 * b'2':   Request for credentials check
 * Data expected: [username length (1 byte) : username... : password length (1 byte) : password....]
 * <p>
 * b'3':   Request for facial image check
 * Data expected: Full png Image file (As-Is, including any header / footer)
 * <p>
 * b'4':   Request for statistics data.
 * Data expected: None
 * <p>
 * b'5':   Request for change password.
 * Data expected:  [username length (1 byte) : username... :
 * old password length (1 byte) : old password.... :
 * new password length (1 byte) : new password...]
 * <p>
 * b'5'     *Request for adding new facial image.
 * Data expected: [username length (1 byte) : username... :
 * old password length (1 byte) : old password.... :
 * Full png Image file (As-Is, including any header / footer)]
 * <p>
 * From server:
 * <p>
 * b'0':   Captcha response from server.
 * Data expected: Full png Image file (As-Is, including any header / footer)
 * <p>
 * b'1':   Captcha-text guess validation response.
 * Data expected: Boolean value (1 byte: b'\x00' for FALSE and b'\x01' for TRUE)
 * <p>
 * b'2':   Credentials validation response.
 * Data expected: Boolean value (1 byte: b'\x00' for FALSE and b'\x01' for TRUE)
 * <p>
 * b'3':   Facial recognition validation response.
 * Data expected: Boolean value (1 byte: b'\x00' for FALSE and b'\x01' for TRUE)
 * <p>
 * b'4':   Return of the statistic data.
 * Data Expected: See static data package description.
 * <p>
 * b'5':   Password change success response.
 * Data expected: Boolean value (1 byte:
 * b'\x00' if the old username & password didn't match
 * b'\x01' if the old username & password were correct and the password updated.
 * <p>
 * b'6':   Facial image addition success response.
 * Data expected: Boolean value (1 byte:
 * b'\x00' if the old username & password didn't match,
 * b'\x01' if the old username & password were correct and the image added.
 */
public interface TransmissionProtocol {

    byte NUM_OF_BYTES_IN_DATA_SIZE = 4;
    byte NUM_OF_BYTES_IN_INT = 4;

    byte CAPTCHA_RESPONSE = 0;
    byte CAPTCHA_REQUEST = 0;

    byte CAPTCHA_TEXT_CHECK_RESPONSE = 1;
    byte CAPTCHA_TEXT_CHECK_REQUEST = 1;

    byte CREDENTIALS_CHECK_RESPONSE = 2;
    byte CREDENTIALS_CHECK_REQUEST = 2;

    byte FACE_IMAGE_CHECK_RESPONSE = 3;
    byte FACE_IMAGE_CHECK_REQUEST = 3;

    byte STATISTICS_DATA_RESPONSE = 4;
    byte STATISTICS_DATA_REQUEST = 4;

    byte CHANGE_PASSWORD_RESPONSE = 5;
    byte CHANGE_PASSWORD_REQUEST = 5;

    byte ADD_IMAGE_RESPONSE = 6;
    byte ADD_IMAGE_REQUEST = 6;


    static byte[] wrapData(byte flag, byte... data) {
        return Utils.concatenate(intToByteArray(data.length), new byte[]{flag}, data);
    }

    static byte[] fregmentStrings(String... strings) {

        byte[] fragmentedStrings = new byte[0];
        for (String string : strings) {
            fragmentedStrings =
                    Utils.concatenate(fragmentedStrings, new byte[]{(byte) string.length()}, string.getBytes());
        }
        return fragmentedStrings;
    }

    static byte[] image2bytes(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    static void bytes2image(byte[] bytes, String path) throws IOException {
        Files.write(Paths.get(path), bytes);
    }

    static byte[] intToByteArray(int n) {
        return ByteBuffer.allocate(NUM_OF_BYTES_IN_INT).putInt(n).array();
    }

    static int byteArrayToInt(byte[] byteArray) {
        return ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).getInt();
    }


    void getCaptcha();

    boolean checkCaptchaTextGuess(String userGuess);

    boolean checkCradentials(String username, String password);

    boolean checkFaceImage(String faceImagePath);

    Object getStatistics();

    boolean changePassword(String username, String oldPassword, String newPassword);

    boolean addImage(String username, String password, String imagePath);

}
