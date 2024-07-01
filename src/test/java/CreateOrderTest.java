import dto.*;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.empty;

public class CreateOrderTest extends Steps {

    @Test
    @DisplayName("Создание заказа")
    @Description("успешный запрос возвращает success: true")
    public void createOrderTest() {
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
        sendHttpPostPojoRq("/api/orders", order, token).then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", equalTo("Бессмертный флюоресцентный бургер"))
                .body("order.ingredients[0]._id", equalTo(firstIngedient))
                .body("order.ingredients[0].name", equalTo("Флюоресцентная булка R2-D3"))
                .body("order.ingredients[0].type", equalTo("bun"))
                .body("order.ingredients[0].proteins", equalTo(44))
                .body("order.ingredients[0].fat", equalTo(26))
                .body("order.ingredients[0].carbohydrates", equalTo(85))
                .body("order.ingredients[0].calories", equalTo(643))
                .body("order.ingredients[0].price", equalTo(988))
                .body("order.ingredients[0].image", equalTo("https://code.s3.yandex.net/react/code/bun-01.png"))
                .body("order.ingredients[0].image_mobile", equalTo("https://code.s3.yandex.net/react/code/bun-01-mobile.png"))
                .body("order.ingredients[0].image_large", equalTo("https://code.s3.yandex.net/react/code/bun-01-large.png"))
                .body("order.ingredients[0].__v", equalTo(0))
                .body("order.ingredients[1]._id", equalTo(secondIngedient))
                .body("order.ingredients[1].name", equalTo("Мясо бессмертных моллюсков Protostomia"))
                .body("order.ingredients[1].type", equalTo("main"))
                .body("order.ingredients[1].proteins", equalTo(433))
                .body("order.ingredients[1].fat", equalTo(244))
                .body("order.ingredients[1].carbohydrates", equalTo(33))
                .body("order.ingredients[1].calories", equalTo(420))
                .body("order.ingredients[1].price", equalTo(1337))
                .body("order.ingredients[1].image", equalTo("https://code.s3.yandex.net/react/code/meat-02.png"))
                .body("order.ingredients[1].image_mobile", equalTo("https://code.s3.yandex" +
                        ".net/react/code/meat-02-mobile.png"))
                .body("order.ingredients[1].image_large", equalTo("https://code.s3.yandex.net/react/code/meat-02-large" +
                        ".png"))
                .body("order.ingredients[1].__v", equalTo(0))
                .body("order._id.__v", is(not(empty())))
                .body("order.owner.name", equalTo(name))
                .body("order.owner.email", equalTo(email.toLowerCase()))
                .body("order.owner.createdAt",
                        containsString(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .body("order.owner.updatedAt",
                        containsString(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .body("order.status", equalTo("done"))
                .body("order.name", equalTo("Бессмертный флюоресцентный бургер"))
                .body("order.createdAt", containsString(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .body("order.updatedAt", containsString(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .body("order.number", is(not(empty())))
                .body("order.price", equalTo(2325));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("успешный запрос возвращает success: true")
    public void createOrderWithoutTokenTest() {
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
        sendHttpPostPojoRq("/api/orders", order).then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", equalTo("Бессмертный флюоресцентный бургер"))
                .body("order.number", is(not(empty())))
                .body("", aMapWithSize(3));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Создание заказа с невалидным хешом ингриедиента")
    @Description("успешный запрос возвращает success: false")
    public void createIncorrectOrderTest() {
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

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("60d3b41abdacab0026a733c6");
        ingredients.add("609646e4dc916e00276b2870");
        OrdersRq order = new OrdersRq(ingredients);
        sendHttpPostPojoRq("/api/orders", order, token).then().assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("One or more ids provided are incorrect"));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));

    }

    @Test
    @DisplayName("Создание заказа с невалидным хешом ингриедиента")
    @Description("успешный запрос возвращает success: false")
    public void createInvalidOrderTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        String firstInvalidIngredient = RandomStringUtils.randomAlphabetic(24);
        String secondInvalidIngredient = RandomStringUtils.randomAlphabetic(24);
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

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(firstInvalidIngredient);
        ingredients.add(secondInvalidIngredient);
        OrdersRq order = new OrdersRq(ingredients);
        sendHttpPostPojoRq("/api/orders", order, token).then().assertThat()
                .statusCode(500)
                .body("html.body.pre", equalTo("Internal Server Error"));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Создание заказа с без ингриедиентов")
    @Description("успешный запрос возвращает success: false")
    public void createOrderWothoutIngredientsTest() {
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

        OrdersRq order = new OrdersRq(null);
        sendHttpPostPojoRq("/api/orders", order, token).then().assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));

    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка схемы POJO по документации")
    public void createOrderSchemaValidationTest() {
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

        OrdersRs responseBody = sendHttpPostPojoRq("/api/orders", order, token)
                .body().as(OrdersRs.class);
        MatcherAssert.assertThat(responseBody, notNullValue());

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }
}
