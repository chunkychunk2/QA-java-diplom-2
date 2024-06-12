import dto.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

public class CreateUserTest extends Steps {

    @Test
    @DisplayName("Создание пользователя")
    @Description("успешный запрос возвращает success: true")
    public void createUserTest() {
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
        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));

    }

    @Test
    @DisplayName("Создание пользователя: валидация")
    @Description("Проверка схемы POJO по документации")
    public void createUserSchemaValidationTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);
        Response response = sendHttpPostPojoRq("/api/auth/register", user);
        RegisterRs responseBody = response.body().as(RegisterRs.class);
        MatcherAssert.assertThat(responseBody, notNullValue());

        String token = response.jsonPath()
                .getString("accessToken");
        response = sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token);

        response.then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
        UserRs secondResponse = response.body().as(UserRs.class);
        MatcherAssert.assertThat(secondResponse, notNullValue());
    }


    @Test
    @DisplayName("Создание зарегистрированного пользователя")
    @Description("успешный запрос возвращает success: false")
    public void createSameUserTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);
        sendHttpPostPojoRq("/api/auth/register", user).then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));

        String token = response.jsonPath()
                .getString("accessToken");
        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));

    }

    @Test
    @DisplayName("Создание зарегистрированного пользователя")
    @Description("Проверка схемы POJO по документации")
    public void createSameUserSchemaValidationTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);
        Response SecondResponse = sendHttpPostPojoRq("/api/auth/register", user);
        RegisterRs responseBody = SecondResponse.body().as(RegisterRs.class);
        MatcherAssert.assertThat(responseBody, notNullValue());

        String token = response.jsonPath()
                .getString("accessToken");
        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));

    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("успешный запрос возвращает success: false")
    public void createUserWithOutEmailTest() {
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(null, password, name);

        sendHttpPostPojoRq("/api/auth/register", user).then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без password")
    @Description("успешный запрос возвращает success: false")
    public void createUserWithOutPasswordTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, null, name);

        sendHttpPostPojoRq("/api/auth/register", user).then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без name")
    @Description("успешный запрос возвращает success: false")
    public void createUserWithOutNameTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, null);

        sendHttpPostPojoRq("/api/auth/register", user).then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
