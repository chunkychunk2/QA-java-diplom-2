import dto.POJO;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class Steps extends Hooks {

    @Step("Отправить GET запрос")
    public Response sendHttpGetRq(String endPoint) {
        return
                given()
                        .when()
                        .get(endPoint);
    }

    @Step("Отправить GET запрос c заголовком")
    public Response sendHttpGetRq(String endPoint, String token) {
        return
                given()
                        .header("Authorization", token)
                        .when()
                        .get(endPoint);
    }

    @Step("Отправить POST запрос c dto")
    public Response sendHttpPostPojoRq(String endPoint, POJO pojo) {
        return
                given()
                        .body(pojo)
                        .when()
                        .post(endPoint);
    }

    @Step("Отправить POST запрос c dto и заголовком")
    public Response sendHttpPostPojoRq(String endPoint, POJO pojo, String token) {
        return
                given()
                        .header("Authorization", token)
                        .body(pojo)
                        .when()
                        .post(endPoint);
    }

    @Step("Отправить PATCH запрос c dto и заголовком")
    public Response sendHttpPatchPojoRq(String endPoint, POJO pojo, String token) {
        return
                given()
                        .header("Authorization", token)
                        .body(pojo)
                        .when()
                        .patch(endPoint);
    }

    @Step("Отправить DELETE запрос и заголовком")
    public Response sendHttpDeleteRq(String endPoint, POJO pojo, String token) {
        return
                given()
                        .header("Authorization", token)
                        .body(pojo)
                        .when()
                        .delete(endPoint);
    }
}
