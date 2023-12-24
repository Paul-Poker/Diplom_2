import client.OrderClient;
import client.UserClient;
import data.OrderData;
import data.UserData;
import data.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static data.IngredientsData.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest extends BaseTest{

    private UserClient userClient;
    private UserData user;
    private OrderClient orderClient;
    private OrderData order;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        orderClient = new OrderClient();
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией с ингредиентами")
    public void createOrderWitAuthAndIngredients(){
        order = new OrderData(ingredients);
        ValidatableResponse createResponse = orderClient.createOrderIsAuth(order, accessToken);
        createResponse
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией без ингредиентов")
    public void createOrderWitAuthWithoutIngredients() {
        order = new OrderData(null);
        ValidatableResponse createResponse = orderClient.createOrderIsAuth(order, accessToken);
        createResponse
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", is(false))
                .and()
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации с ингредиентами")
    public void createOrderWithIngredientsWithoutAuth() {
        order = new OrderData(ingredients);
        ValidatableResponse createResponse = orderClient.createOrderWithoutAuth(order);
        createResponse
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации без ингредиенов")
    public void createOrderWithoutAuthAndInfredients() {
        order = new OrderData(null);
        ValidatableResponse createResponse = orderClient.createOrderWithoutAuth(order);
        createResponse
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", is(false))
                .and()
                .body("message", is("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Проверка создания заказа с авторизацией с невалидным хешем ингредиентов")
    public void createOrderWitAuthAndNoValidHashOfIngredients() {
        order = new OrderData(noValidIngredients);
        ValidatableResponse createResponse = orderClient.createOrderIsAuth(order, accessToken);
        createResponse
                .assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }



}
