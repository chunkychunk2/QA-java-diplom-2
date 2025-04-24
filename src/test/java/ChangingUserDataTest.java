import dto.RegisterRq;
import dto.UserRq;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.empty;

public class ChangingUserDataTest extends Steps{

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("успешный запрос возвращает success: true")
    public void changeUserDataTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String changedEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);

        UserRq changedData = new UserRq(changedEmail, password, name);

        String token = response.jsonPath()
                .getString("accessToken");

        sendHttpPatchPojoRq("/api/auth/user",changedData, token).then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(changedEmail.toLowerCase()))
                .body("user.name", equalTo(name));

        sendHttpGetRq("/api/auth/user", token).then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(changedEmail.toLowerCase()))
                .body("user.name", equalTo(name));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));

        sendHttpGetRq("/api/auth/user", token).then().assertThat()
                .statusCode(404)
                .body("success", equalTo(false))
                .body("message", equalTo("User not found"));
    }

    @Test
    @DisplayName("Изменение данных пользователя без токена")
    @Description("успешный запрос возвращает success: false")
    public void changeUserDataWithOutTokenTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String changedEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);

        UserRq changedData = new UserRq(changedEmail, password, name);

        String token = response.jsonPath()
                .getString("accessToken");

        sendHttpPatchPojoRq("/api/auth/user",changedData, "").then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));

        sendHttpGetRq("/api/auth/user", token).then().assertThat()
                .statusCode(404)
                .body("success", equalTo(false))
                .body("message", equalTo("User not found"));
    }

    @Test
    @DisplayName("Изменение данных пользователя с некорректным токеном")
    @Description("успешный запрос возвращает success: false")
    public void changeUserDataWithIncorrectTokenTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String changedEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);

        UserRq changedData = new UserRq(changedEmail, password, name);

        String token = response.jsonPath()
                .getString("accessToken");

        sendHttpPatchPojoRq("/api/auth/user",changedData, token.replace("Bearer ","Bearer 1")).then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("invalid token"));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));

        sendHttpGetRq("/api/auth/user", token).then().assertThat()
                .statusCode(404)
                .body("success", equalTo(false))
                .body("message", equalTo("User not found"));
    }

    @Test
    @DisplayName("Изменение данных пользователя с некорректным токеном")
    @Description("успешный запрос возвращает success: false")
    public void changeUserDataWithInvalidTokenTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String changedEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);

        UserRq changedData = new UserRq(changedEmail, password, name);

        String token = response.jsonPath()
                .getString("accessToken");

        sendHttpPatchPojoRq("/api/auth/user",changedData, token+1).then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("invalid signature"));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));

        sendHttpGetRq("/api/auth/user", token).then().assertThat()
                .statusCode(404)
                .body("success", equalTo(false))
                .body("message", equalTo("User not found"));
    }

    @Test
    @DisplayName("Изменение данных пользователя с невалидным токеном")
    @Description("успешный запрос возвращает success: false")
    public void changeUserDataWithMalformedTokenTest() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String changedEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterRq user = new RegisterRq(email, password, name);

        Response response = sendHttpPostPojoRq("/api/auth/register", user);

        UserRq changedData = new UserRq(changedEmail, password, name);

        String token = response.jsonPath()
                .getString("accessToken");

        sendHttpPatchPojoRq("/api/auth/user",changedData, "malformed token").then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("jwt malformed"));

        sendHttpDeleteRq("/api/auth/user", new UserRq(email, password, name), token).then().assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));

        sendHttpGetRq("/api/auth/user", token).then().assertThat()
                .statusCode(404)
                .body("success", equalTo(false))
                .body("message", equalTo("User not found"));
    }
}
