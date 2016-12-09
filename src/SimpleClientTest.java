import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by kfir on 08/12/16.
 */
class SimpleClientTest {

    private SimpleClient simpleClient;

    @BeforeEach
    void setUp() throws IOException {
        this.simpleClient = new SimpleClient("127.0.0.1", 9999);
    }

    @AfterEach
    void tearDown() throws IOException {
        this.simpleClient.close();
    }

    @Test
    void getCaptcha() {
        // See the image captcha.png in folder temp
        this.simpleClient.getCaptcha();
    }

    @Test
    void checkCaptchaTextGuess() {
    }

    @Test
    void checkCradentials() {
        System.out.println();
    }

    @Test
    void checkFaceImage() {

    }

    @Test
    void getStatistics() {

    }

    @Test
    void changePassword() {
        System.out.println(
                this.simpleClient.changePassword(
                        "testUsername", "testPassword", "testPassword!")
        );
    }

    @Test
    void addImage() {

    }

    @Test
    void addUser() {
        // Print True if the process succeed
        System.out.println(
                this.simpleClient.addUser("admin", "password",
                        "testFirstName", "testLastName", "testUsername", "testPassword", 1,
                        TransmissionProtocol.USER)
        );
    }
}