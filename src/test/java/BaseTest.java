import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;

public class BaseTest {
    private String accessToken;

    @After
    @DisplayName("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        }
    }
}
