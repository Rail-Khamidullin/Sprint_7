package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.api.CourierJSON;
import org.example.courier.SupportCourierMethods;
import org.junit.*;
import org.junit.runners.MethodSorters;
import static org.example.Constants.SAMOKAT_URL;

// Создание курьера
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateCourierTest {

    // Экземпляр класса с методами по курьеру
     final SupportCourierMethods supportCourierMethods = new SupportCourierMethods();

    // Повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
    @Before
    public void setUp() {
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = SAMOKAT_URL;
    }

    /// Создание курьера
    @Test
    @DisplayName("Create courier") // имя теста
    @Description("Создание курьера (позитивный сценарий)") // описание теста
    public void a_CreateCourier() {
        // Отправляем запрос на создание
        supportCourierMethods.setCourierBody(new CourierJSON("Rail63", "1111", "saske"));
        Response response = supportCourierMethods.сreateCourier();
        // Достаём необхадимую информацию
        supportCourierMethods.getCreateResponseTrue(response);
    }

    /// Создание курьера с отсутствующим логином
    @Test
    @DisplayName("Create courier without login") // имя теста
    @Description("Создание курьера без логина (негативный сценарий)") // описание теста
    public void b_CreateCourierWithoutLogin() {
        // Указываем тело запроса
        supportCourierMethods.setCourierBody(new CourierJSON("", "1111", "saske"));
        // Отправляем запрос на создание
        Response response = supportCourierMethods.сreateCourier();
        // Достаём необхадимую информацию
        supportCourierMethods.getResponseMessageWithoutData(response);
    }

    /// Создание дубля курьера
    @Test
    @DisplayName("Create double courier") // имя теста
    @Description("Создание дубля курьера (негативный сценарий)") // описание теста
    public void c_CreateDoubleCourier() {
        supportCourierMethods.setCourierBody(new CourierJSON("Rail63", "1111", "saske"));
        // Отправляем запрос на создание
        supportCourierMethods.сreateCourier();
        // Отправляем запрос на создание
        supportCourierMethods.сreateCourier();
        Response response = supportCourierMethods.сreateCourier();
        // Достаём необхадимую информацию
        supportCourierMethods.getResponseDoubleCourier(response);
        supportCourierMethods.deleteCourier();
    }

    // Удаление ранее созданных тестовых данных
    @After
    public void afterClass() throws Exception {
        // Удаление ранее созданных тестовых данных
        supportCourierMethods.deleteCourier();
    }
}
