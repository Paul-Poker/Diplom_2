import client.UserClient;
import data.UserData;
import data.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UpdateUserTest extends BaseTest{
    private UserClient userClient;
    private UserData user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
    }

    @Test
    @DisplayName("Проверка апдейта имени авторизованного пользователя")
    public void successfulUpdateUserDataIsAut(){

        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        user.setName("Name");
        ValidatableResponse responseUpdateName = userClient.updateUserDataIsAuth(user, accessToken);
        responseUpdateName
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Проверка апдейта данных неавторизованного пользователя")
    public void unsuccessfulUpdateUserDataWithoutAuth() {

        ValidatableResponse responseWithoutAuth= userClient.updateUserDataWithoutAuth(user);
        user.setName("Name");
        responseWithoutAuth
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("You should be authorised"));
    }


}
