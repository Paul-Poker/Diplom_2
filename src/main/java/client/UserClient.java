package client;


import data.UserCredentials;
import data.UserData;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient{

    private static final String USER_CREATE = "/api/auth/register";
    private static final String USER_LOGIN = "/api/auth/login";
    private static final String USER_UPDATE_DELETE = "/api/auth/user";


    @Step("Создание пользователя")
    public ValidatableResponse createUser(UserData user) {
        return given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .post(USER_CREATE)
                .then();
    }
    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(UserCredentials credentials) {
        return given()
                .spec(requestSpecification())
                .body(credentials)
                .when()
                .post(USER_LOGIN)
                .then();
    }

    @Step("Изменение данных пользователя с авторизацией")
    public static ValidatableResponse updateUserDataIsAuth(UserData user, String accessToken) {
        return given()
                .spec(requestSpecification())
                .header("authorization", accessToken)
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .patch(USER_UPDATE_DELETE)
                .then();
    }
    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse updateUserDataWithoutAuth(UserData user) {
        return given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .patch(USER_UPDATE_DELETE)
                .then();
    }


    @Step("Удаление пользователя")
    public static void deleteUser(String accessToken) {
        given()
                .spec(requestSpecification())
                .header("authorization", accessToken)
                .auth().oauth2(accessToken)
                .when()
                .delete(USER_UPDATE_DELETE)
                .then();
    }
}
