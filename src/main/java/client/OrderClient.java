package client;

import data.OrderData;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static client.RestClient.requestSpecification;
import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDER_CREATE = "/api/orders";

    @Step("Создание заказа с авторизацией")
    public static ValidatableResponse createOrderIsAuth(OrderData orderData, String accessToken) {
        return given()
                .spec(requestSpecification())
                .header("authorization", accessToken)
                .body(orderData)
                .post(ORDER_CREATE)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(OrderData orderData) {
        return given()
                .spec(requestSpecification())
                .body(orderData)
                .post(ORDER_CREATE)
                .then();
    }

    @Step("Получение заказа авторизованного пользователя")
    public static ValidatableResponse getOrderWithAuth(String accessToken) {
        return given()
                .spec(requestSpecification())
                .header("authorization", accessToken)
                .when()
                .get(ORDER_CREATE)
                .then();
    }

    @Step("Получение заказа неавторизованного пользователя")
    public ValidatableResponse getOrderWithoutAuth() {
        return given()
                .spec(requestSpecification())
                .get(ORDER_CREATE)
                .then();
    }

}
