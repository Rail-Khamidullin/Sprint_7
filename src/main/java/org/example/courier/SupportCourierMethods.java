package org.example.courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.api.Courier;
import org.example.api.CourierJSON;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class SupportCourierMethods implements SupportCourier {

    // API курьера
    public static final String API_COURIER       = "/api/v1/courier";
    public static final String API_COURIER_LOGIN = "/api/v1/courier/login";

    // Создаём экземпляр класса с телом запроса
    private CourierJSON courierJSON;
    // Устанавливаем необходимые значения тела
    public void setCourierBody(CourierJSON courierJSON) { this.courierJSON = courierJSON; }

    @Step("Создание курьера")
    public Response сreateCourier() {
        Response response = given()
                .header("Content-type", "application/json")
                .log().all()
                .body(courierJSON)
                .when()
                .post(API_COURIER);
        return response;
    }

    @Step("Получаем ответ true и статус код 201")
    public void getCreateResponseTrue(Response response) {
        response.then().assertThat().body("ok",equalTo(true)).statusCode(201);
    }

    // Шаг для создания курьера с недостатком данных
    @Step("Получаем ответ с текстом 'Недостаточно данных для создания учетной записи' и статус код 400")
    public void getResponseMessageWithoutData(Response response) {
        response.then().assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи")).statusCode(400);
    }

    // Шаг для создания дубля
    @Step("Получаем ошибку на создание дубля с текстом 'Этот логин уже используется' и статус 409")
    public void getResponseDoubleCourier(Response response) {
        response.then().assertThat().body("message", equalTo("Этот логин уже используется")).statusCode(409);
    }

    @Step("Отправляем запрос на получение ID курьера")
    public Response getLoginCourier() {
        Response response = given()
                .header("Content-type", "application/json")
                .log().all()
                .body(courierJSON)
                .when()
                .post(API_COURIER_LOGIN);
        return response;
    }

    @Step("Проверяем наличие ID")
    public void getLoginCourierTrue(Response response) {
        response.then().assertThat().body("id", notNullValue()).statusCode(200);
    }

    @Step("Получаем ошибку если в теле отсутствует логин или пароль")
    public void getLoginCourierNotFullBody(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).statusCode(400);
    }

    @Step("Получаем ошибку если в теле ID не существующий курьер")
    public void getLoginCourierWithBugID(Response response) {
        response.then().assertThat().body("message",equalTo("Учетная запись не найдена")).statusCode(404);
    }

    // Удаляем курьера из БД
    public void deleteCourier() {

        Integer idCourier = getLoginCourier().body().as(Courier.class).getId();
        if (idCourier > 0 ) {
            // Удаляем курьера
            Response response = given()
                    .header("Content-type", "application/json")
                    .log().all()
                    .pathParam("id", idCourier)
                    .when()
                    .delete(API_COURIER +"/{id}");
            response.then().statusCode(200);
            // Логируем ответ
            System.out.println("Delete Response: " + response.body().asString());
            response.then().statusCode(200); // Проверяем статус код
        } else {
            System.out.println("Courier is null!");
        }
    }
}
