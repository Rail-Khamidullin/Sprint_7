package org.example.order;

import io.restassured.response.Response;

public interface SupportOrder {
    // Создание заказа
    public Response сreateOrder();
    // Получаем ответ с номером заказа и статус кодом 201
    public void createOrderResponse(Response response);
    // Получение заказа по трек номеру
    public Response getIDOrder(Response response);
    // Курьер принимает заказ по ID номеру
    public void getAcceptOrder(Response iDCourier, Response idOrderCourier);
    // Курьер получает список заказов по своему ID
    public Response getOrderList(Response responseLogin);
    // Проверяем, что список не пустой и статус код 200
    public void checkOrderList(Response response);
    // Запрос на отмену заказа по трек номеру заказа
    public void cancelOrder(Response response);
}
