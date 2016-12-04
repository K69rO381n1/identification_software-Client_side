import java.io.IOException;


class SimpleClientTest {
    @org.junit.jupiter.api.Test
    void getCaptcha() throws IOException {
        TransmissionProtocol a = new SimpleClient("127.0.0.1", 9999);
        a.getCaptcha();
        a.close();
    }

    @org.junit.jupiter.api.Test
    void checkCaptchaTextGuess() {

    }

    @org.junit.jupiter.api.Test
    void checkCradentials() {

    }

    @org.junit.jupiter.api.Test
    void checkFaceImage() {

    }

    @org.junit.jupiter.api.Test
    void getStatistics() {

    }

    @org.junit.jupiter.api.Test
    void changePassword() {

    }

    @org.junit.jupiter.api.Test
    void addImage() {

    }

}