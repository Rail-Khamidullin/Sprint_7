package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.api.*;
import org.example.courier.SupportCourierMethods;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class SupportOrderMethods implements SupportOrder {

    private SupportCourierMethods supportCourierMethods = new SupportCourierMethods();

    // Тело запроса заказа
    public OrderJSON orderJSON;

    // Заполнить тело запроса заказа
    public void setOrderBody(OrderJSON orderJSON) { this.orderJSON = orderJSON; }

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

    @Step("Получение заказа по трек номеру")
    public Response getIDOrder(Response response) {

        Integer track = response.body().as(Track.class).getTrack();

        Response responseWithTrack = given()
                .log().all()
                .when()
                .get("/api/v1/orders/track?t={track}", track);
        return responseWithTrack;
    }

    @Step("Курьер принимает заказ по ID номеру")
    public void getAcceptOrder(Response iDCourier, Response idOrderCourier) {

        // Получаем ID курьера
        Integer idCourier = iDCourier.body().as(Courier.class).getId();
        // Достаём ID заказа
        Order idOrder = idOrderCourier.body().as(Order.class);
        Integer id = idOrder.getId();

        System.out.println(idOrderCourier.body().asString());
        System.out.println("ID заказа = " + idOrder);
        System.out.println("Имя = " + idOrder.getFirstName());

        given()
                .log().all()
                .pathParams("courierId", idCourier)
                .pathParams("id",idOrder)
                .when()
                .put("/api/v1/orders/accept/:{id}?courierId={courierId}");
    }

    /// Получить список заказов
    @Step("Курьер получает список заказов по своему ID")
    public Response getOrderList() {
        // Получаем ID курьера
        Integer idCourier = supportCourierMethods.getLoginCourier().body().as(Courier.class).getId();

        Response response = given()
                .log().all()
                .when()
                .get("/api/v1/orders?courierId={id}&nearestStation=2&limit=30&page=0", idCourier);
        return response;
    }

    @Step("Проверяем, что список не пустой и статус код 200")
    public void checkOrderList(Response response) {
        response.then().assertThat()
                .body("orders", notNullValue())
                .statusCode(200);
        System.out.println(response.body().asString());
    }

    @Step("Запрос на отмену заказа")
    public void cancelOrder(Response response) {
        int track = response.body().as(CancelJSON.class).getTrack();

        given()
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .put("/api/v1/orders/cancel?track={track}", track);

        System.out.println(track);
    }
}
