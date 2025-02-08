package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class SupportOrderMethods implements SupportOrder {

    // Тело запроса
    public OrderJSON orderJSON;

    // Заполнить тело запроса
    public void setOrderBody(OrderJSON orderJSON) {
        this.orderJSON = orderJSON;
    }

    /// Создание заказа
    @Step("Создание заказа")
    public Response сreateOrder() {
        Response response = given()
                .header("Content-type", "application/json")
                .log().all()
                .body(orderJSON)
                .when()
                .post("/api/v1/orders");
        return response;
    }

    @Step("Получаем ответ с номером заказа и статус кодом 201")
    public void createOrderResponse(Response response) {
        response.then().assertThat().body("track", notNullValue()).statusCode(201);
    }
}
