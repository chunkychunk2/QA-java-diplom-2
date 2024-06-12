import dto.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

public class TakeOrderTest extends Steps {

    @Test
    @DisplayName("Получение заказа")
    @Description("успешный запрос возвращает success: true")
    public void takeOrderTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name))
                .body("accessToken", is(not(empty())))
                .body("refreshToken", is(not(empty())));

        String token = response.jsonPath()
                .getString("accessToken");

        response = sendHttpGetRq("api/ingredients");

        String firstIngedient = response.jsonPath().getString("data[0]._id");
        String secondIngedient = response.jsonPath().getString("data[1]._id");

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngedient);
        ingredients.add(secondIngedient);
        OrdersRq order = new OrdersRq(ingredients);
        sendHttpPostPojoRq("/api/orders", order, token);

        sendHttpGetRq("/api/orders",token).then().assertThat()
                        .statusCode(200)
                                .body("success",equalTo(true))
                                .body("orders[0]._id",is(not(empty())))
                                .body("orders[0].ingredients[0]",equalTo(firstIngedient))
                                .body("orders[0].ingredients[1]",equalTo(secondIngedient))
                                .body("orders[0].status",equalTo("done"))
                                .body("orders[0].name",equalTo("Бессмертный флюоресцентный бургер"))
                                .body("orders[0].createdAt",containsString(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                                .body("orders[0].updatedAt",containsString(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                                .body("orders[0].number",is(not(empty())))
                                .body("total",is(not(empty())))
                                .body("totalToday",is(not(empty())));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Получение заказа без авторизации")
    @Description("успешный запрос возвращает success: false")
    public void takeOrderWithOutAuthTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name))
                .body("accessToken", is(not(empty())))
                .body("refreshToken", is(not(empty())));

        String token = response.jsonPath()
                .getString("accessToken");

        response = sendHttpGetRq("api/ingredients");

        String firstIngedient = response.jsonPath().getString("data[0]._id");
        String secondIngedient = response.jsonPath().getString("data[1]._id");

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngedient);
        ingredients.add(secondIngedient);
        OrdersRq order = new OrdersRq(ingredients);
        sendHttpPostPojoRq("/api/orders", order, token);

        sendHttpGetRq("/api/orders").then().assertThat()
                .statusCode(401)
                .body("success",equalTo(false))
                .body("message",equalTo("You should be authorised"));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }


    @Test
    @DisplayName("Получение заказа")
    @Description("Проверка схемы POJO по документации")
    public void takeOrderSchemaValidationTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name))
                .body("accessToken", is(not(empty())))
                .body("refreshToken", is(not(empty())));

        String token = response.jsonPath()
                .getString("accessToken");

        response = sendHttpGetRq("api/ingredients");

        String firstIngedient = response.jsonPath().getString("data[0]._id");
        String secondIngedient = response.jsonPath().getString("data[1]._id");

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngedient);
        ingredients.add(secondIngedient);
        OrdersRq order = new OrdersRq(ingredients);
        sendHttpPostPojoRq("/api/orders", order, token);

        OrdersRs responseBody = sendHttpGetRq("/api/orders",token).body().as(OrdersRs.class);
        MatcherAssert.assertThat(responseBody, notNullValue());

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

}
