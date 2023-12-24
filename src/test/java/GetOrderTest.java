import client.OrderClient;
import client.UserClient;
import data.OrderData;
import data.UserCredentials;
import data.UserData;
import data.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static data.IngredientsData.ingredients;
import static org.hamcrest.CoreMatchers.is;

public class GetOrderTest extends BaseTest{

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
        order = new OrderData(ingredients);
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
    }
    @Test
    @DisplayName("Проверка получения заказов авторизованного пользователя")
    public void getOrdersAuthUser() {
        UserCredentials.from(user);
        OrderClient.createOrderIsAuth(order, "accessToken");
        ValidatableResponse createResponse = orderClient.getOrderWithAuth(accessToken);
        createResponse
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Проверка получения заказов неавторизованного пользователя")
    public void getOrderUnauthUser() {
        ValidatableResponse createResponse = orderClient.getOrderWithoutAuth();
        createResponse
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("You should be authorised"));
    }

}
