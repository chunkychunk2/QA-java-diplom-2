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

public class LoginTest extends Steps {

    @Test
    @DisplayName("Логин пользователя")
    @Description("успешный запрос возвращает success: true")
    public void LoginUserTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);

        LogInRq login = new LogInRq(email, password);

        sendHttpPostPojoRq("/api/auth/login", login).then().assertThat()
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
    @DisplayName("Логин пользователя: валидация")
    @Description("Проверка схемы POJO по документации")
    public void loginUserSchemaValidationTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);
        sendHttpPostPojoRq("/api/auth/register", user);

        LogInRq login = new LogInRq(email, password);

        Response response =  sendHttpPostPojoRq("/api/auth/login", login);

        LogInRs responseBody = response.body().as(LogInRs.class);
        MatcherAssert.assertThat(responseBody, notNullValue());

        String token = response.jsonPath()
                .getString("accessToken");
        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Логин пользователя без email")
    @Description("успешный запрос возвращает success: false")
    public void loginUserWithOutEmailTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);

        LogInRq login = new LogInRq(null, password);

        sendHttpPostPojoRq("/api/auth/login", login).then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

        String token = response.jsonPath()
                .getString("accessToken");
        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Логин пользователя без password")
    @Description("успешный запрос возвращает success: false")
    public void loginUserWithOutPasswordTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);

        LogInRq login = new LogInRq(email, null);

        sendHttpPostPojoRq("/api/auth/login", login).then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

        String token = response.jsonPath()
                .getString("accessToken");
        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Логин пользователя с неверным логином")
    @Description("успешный запрос возвращает success: false")
    public void loginUserWithIncorrectLoginTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String incorrectEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);

        LogInRq login = new LogInRq(incorrectEmail, password);

        sendHttpPostPojoRq("/api/auth/login", login).then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

        String token = response.jsonPath()
                .getString("accessToken");
        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Логин пользователя с неверным паролем")
    @Description("успешный запрос возвращает success: false")
    public void loginUserWithIncorrectPasswordTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String incorrectPassword = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);

        LogInRq login = new LogInRq(email, incorrectPassword);

        sendHttpPostPojoRq("/api/auth/login", login).then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

        String token = response.jsonPath()
                .getString("accessToken");
        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }
}
