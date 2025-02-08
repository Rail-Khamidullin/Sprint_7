package org.example.courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.api.Courier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class SupportCourierCourierMethods implements SupportCourier {

    // Создаём экземпляр класса с телом запроса
    private CourierJSON courierJSON;
    // Устанавливаем необходимые значения тела
    public void setCourierBody(CourierJSON courierJSON) { this.courierJSON = courierJSON; }

    /// Создание курьера
    @Step("Создание курьера")
    public Response сreateCourier() {
        Response response = given()
                .header("Content-type", "application/json")
                .log().all()
                .body(courierJSON)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    // Получаем ожидаемый ответ {"ok":true}" и статус кода 201
    @Step("Получаем ответ true and статус код 201")
    public void getCreateResponseTrue(Response response) {
        response.then().assertThat().body("ok",equalTo(true)).statusCode(201);
    }

    // Шаг для создания курьера с недостатком данных
    @Step("Get a response with text 'Недостаточно данных для создания учетной записи' and status 400")
    public void getResponseMessageWithoutData(Response response) {
        response.then().assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи")).statusCode(400);
    }

    // Шаг для создания дубля
    @Step("Получаем ошибку на создание дубля с текстом 'Этот логин уже используется' и статус 409")
    public void getResponseDoubleCourier(Response response) {
        response.then().assertThat().body("message", equalTo("Этот логин уже используется")).statusCode(409);
    }

    /// Достаём id курьера и передаём далее
    @Step("Отправляем запрос на получение ID курьера")
    public Response getLoginCourier() {
        Response response = given()
                .header("Content-type", "application/json")
                .log().all()
                .body(courierJSON)
                .when()
                .post("/api/v1/courier/login");
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
        // Получаем id курьера
        Integer idCourier = getLoginCourier().body().as(Courier.class).getId();

        if (idCourier != null) {
            // Удаляем курьера
            Response response = given()
                    .header("Content-type", "application/json")
                    .log().all()
                    .when()
                    .delete("/api/v1/courier/{id}", idCourier);
            response.then().statusCode(200);
            System.out.println(response.body().asString());
        }
    }
}
