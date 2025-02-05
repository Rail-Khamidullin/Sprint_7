package org.example;

import io.restassured.response.Response;

public interface Support {

    // Создание курьера
    public Response сreateCourier();
    // Достаём id курьера
    public Response getLoginCourier();
    // Удаляем курьера из БД
    public void deleteCourier();
}
