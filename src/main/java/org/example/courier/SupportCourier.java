package org.example.courier;

import io.restassured.response.Response;

public interface SupportCourier {

    // Создание курьера
    public Response сreateCourier();
    // Достаём id курьера
    public Response getLoginCourier();
    // Удаляем курьера из БД
    public void deleteCourier();
}
