import client.UserClient;
import data.UserData;
import data.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class CreateUserTest extends BaseTest {
    private UserClient userClient;
    private UserData user;
    private String accessToken;


    @Before
    public void setUp() {
       userClient = new UserClient();
       user = UserGenerator.getRandomUser();
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    public void userCanBeCreated(){

        ValidatableResponse createResponse = userClient.createUser(user);
        createResponse.assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Проверка создания уже имеющегося пользователя")
    public void cannotCreateDuplicateUser() {

        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        createResponse.assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        ValidatableResponse duplicateCreateResponse = userClient.createUser(user);
        duplicateCreateResponse.assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("User already exists"));
    }
    @Test
    @DisplayName("Проверка создания пользователя без емейла, логина или пароля")
    public void cannotCreateUserWithoutEmailOrPasswordOrName() {

        UserData userWithoutEmail = new UserData(null, "password", "Name");

        ValidatableResponse responseWithoutEmail = userClient.createUser(userWithoutEmail);
        responseWithoutEmail.assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));

        UserData userWithoutPassword = new UserData("email@yandex.ru", null, "Name");

        ValidatableResponse responseWithoutPassword = userClient.createUser(userWithoutPassword);
        responseWithoutPassword.assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));


        UserData userWithoutName = new UserData("email@yandex.ru", "password", null);

        ValidatableResponse responseWithoutName = userClient.createUser(userWithoutName);
        responseWithoutName.assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));


    }


}
