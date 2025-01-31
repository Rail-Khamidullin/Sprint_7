package org.example;

import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class SupportClass {

//    // достаём логин курьера
//    public void gdfg(int idCourier) {
//
//        String json = "src/main/resources/loginCourierBody.json "+ idCourier +"";
//
//        Response response =
//                given()
//                        .header("Content-type", "application/json")
//                        .and()
//                        .body(json)
//                        .when()
//                        .post("/api/v1/courier/login");
//        response.then().assertThat().body("id", notNullValue())
//                .and()
//                .statusCode(200);
//        System.out.println(response.body().asString());
//    }

    // Достаём id курьера и передаём далее
    public int getLoginCourier() {
        String json = "src/main/resources/loginCourierBody.json ";

        Courier courier = given()
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login")
                .body().as(Courier.class);  // код для десериализации ответа в объект типа Courier
        // Для проверки, что объект ненулевой, используем матчер assert
        MatcherAssert.assertThat(courier.getId(), notNullValue());
        System.out.println(courier.getId());
        return courier.getId();
    }

    // Удаляем курьера из БД
    public void deleteCourier() {
        // Получаем id курьера
        int idCourier = getLoginCourier();

        String json = "src/main/resources/loginCourierBody.json "+ idCourier +"";

        // удаляем курьера
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .delete("/api/v1/courier/");
        response.then().statusCode(200);
        System.out.println(response.body().asString());
    }
}
