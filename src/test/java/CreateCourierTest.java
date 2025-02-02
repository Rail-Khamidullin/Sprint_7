import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.SupportClass;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateCourierTest {

    // Повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
    @Before
    public void setUp() {
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    // Создание курьера
    @Test
    @DisplayName("Create courier") // имя теста
    @Description("Создание курьера (позитивный сценарий)") // описание теста
    public void a_CreateCourier() {
        // Отправляем запрос на создание
        Response response = sendPostRequestCreateCourier();
        // Достаём необхадимую информацию
        getResponseTrue(response);
    }
    
    // Метод для шага "Отправить запрос":
    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCreateCourier(){
        File json = new File("src/test/resources/courierBody.json");
        Response response = given().header("Content-type", "application/json").body(json).when().post("/api/v1/courier");
        return response;
    }

    // Метод для шага "Получить ожидаемый ответ {"ok":true}" и статус кода 201
    @Step("Get a response true and status 201")
    public void getResponseTrue(Response response) {
        response.then().assertThat().body("ok",equalTo(true)).statusCode(201);
    }

    // Создание курьера с отсутствующим логином
    @Test
    @DisplayName("Create courier without login") // имя теста
    @Description("Создание курьера без логина (негативный сценарий)") // описание теста
    public void b_CreateCourierWithoutLogin() {
        // Отправляем запрос на создание
        Response response = sendPostRequestCreateCourierWithoutLogin();
        // Достаём необхадимую информацию
        getResponseMessageWithoutData(response);
    }

    // Метод для шага "Отправить запрос":
    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCreateCourierWithoutLogin(){
        String json = "{\"password\": \"1111\", \"firstName\": \"Rail1\"}";
        Response response = given().header("Content-type", "application/json").body(json).when().post("/api/v1/courier");
        return response;
    }

    // Метод для шага получения ожидаемого ответа c текстом "Недостаточно данных для создания учетной записи" и статус кода 400
    @Step("Get a response with text 'Недостаточно данных для создания учетной записи' and status 400")
    public void getResponseMessageWithoutData(Response response) {
        response.then().assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи")).statusCode(400);
    }

    // Создание дубля курьера
    @Test
    public void c_CreateDoubleCourier() {
        // Отправляем запрос на создание
        Response response = sendPostRequestCreateDoubleCourier();
        // Достаём необхадимую информацию
        getResponseDoubleCourier(response);
    }

    // Метод для шага "Отправить запрос для создания дубля курьера":
    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCreateDoubleCourier() {
        File json = new File("src/test/resources/courierBody.json");
        Response response = given().header("Content-type", "application/json").body(json).when().post("/api/v1/courier");
        return response;
    }

    // Метод для шага получения ожидаемого ответа c текстом "Этот логин уже используется" и статус кода 409
    @Step("Get a response with text 'Этот логин уже используется' and status 409")
    public void getResponseDoubleCourier(Response response) {
        response.then().assertThat().body("message", equalTo("Этот логин уже используется")).statusCode(409);
    }

    // Удаление ранее созданных тестовых данных
    @AfterClass
    public static void afterClass() throws Exception {
        SupportClass supportClass = new SupportClass();
        supportClass.getLoginCourier();
        supportClass.deleteCourier();
    }
}
