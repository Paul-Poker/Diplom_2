import client.UserClient;
import data.UserCredentials;
import data.UserData;
import data.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest extends BaseTest{
    private UserClient userClient;
    private UserData user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        userClient.createUser(user);
    }
    @Test
    @DisplayName("Проверка авторизации существующего пользователя")
    public void successfulLoginUser(){
        UserCredentials validCredentials = UserCredentials.from(user);
        userClient.loginUser(validCredentials).assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }
    @Test
    @DisplayName("Проверка авторизации пользователя с неверным логином или паролем")
    public void unsuccessfulLoginUserWithoutEmailOrPassword(){

        UserCredentials credentialsWithoutEmail = new UserCredentials("", "password");
        userClient.loginUser(credentialsWithoutEmail).assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"));

        UserCredentials credentialsWithoutPassword = new UserCredentials("email@yandex.ru", "");
        userClient.loginUser(credentialsWithoutPassword).assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"));

    }

}
