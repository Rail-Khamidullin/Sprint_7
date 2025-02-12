package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class SupportOrderMethods implements SupportOrder {

    // API заказа
    public static final String API_ORDERS        = "/api/v1/orders";
    public static final String API_ORDERS_TRACK  = "/api/v1/orders/track";
    public static final String API_ORDERS_ACCEPT = "/api/v1/orders/accept";
    public static final String API_ORDERS_CANCEL = "/api/v1/orders/cancel";

    // Тело запроса заказа
    public OrderJSON orderJSON;

    // Заполнить тело запроса заказа
    public void setOrderBody(OrderJSON orderJSON) { this.orderJSON = orderJSON; }

    @Step("Создание заказа")
    public Response сreateOrder() {

        Response response = given()
                .header("Content-type", "application/json")
                .log().all()
                .body(orderJSON)
                .when()
                .post(API_ORDERS);
        return response;
    }

    @Step("Получаем ответ с номером заказа и статус кодом 201")
    public void createOrderResponse(Response response) {
        response.then().assertThat().body("track", notNullValue()).statusCode(201);
    }

    @Step("Получение заказа по трек номеру")
    public Response getIDOrder(Response response) {
        // Достаём трек номер заказа
        Integer track = response.body().as(Track.class).getTrack();

        Response responseWithTrack = given()
                .log().all()
                .queryParam("t", track)
                .when()
                .get(API_ORDERS_TRACK);
        return responseWithTrack;
    }

    @Step("Курьер принимает заказ по ID номеру")
    public void getAcceptOrder(Response iDCourier, Response idOrderCourier) {
        // Получаем ID курьера
        Integer idCourier = iDCourier.body().as(Courier.class).getId();
        // Достаём ID заказа
        Integer idOrder = idOrderCourier.body().as(Root.class).order.getId();

        given()
                .log().all()
                .queryParam("courierId", idCourier)
                .pathParams("id", idOrder)
                .when()
                .put(API_ORDERS_ACCEPT + "/{id}");
    }

    @Step("Курьер получает список заказов по своему ID")
    public Response getOrderList(Response responseLogin) {
        // Получаем ID курьера
        Integer idCourier = responseLogin.body().as(Courier.class).getId();

        Response response = given()
                .log().all()
                .queryParam("courierId", idCourier)
                .queryParam("nearestStation", 2)
                .queryParam("limit", 30)
                .queryParam("page", 0)
                .when()
                .get(API_ORDERS);
        return response;
    }

    @Step("Проверяем, что список не пустой и статус код 200")
    public void checkOrderList(Response response) {
        response.then().assertThat()
                .body("orders", notNullValue())
                .statusCode(200);
        System.out.println(response.body().asString());
    }

    @Step("Запрос на отмену заказа по трек номеру заказа")
    public void cancelOrder(Response response) {
        // Достаём трек номер заказа
        int track = response.body().as(CancelJSON.class).getTrack();

        given()
                .header("Content-type", "application/json")
                .log().all()
                .queryParam("track", track)
                .when()
                .put(API_ORDERS_CANCEL);
        System.out.println(track);
    }
}
