package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.api.CourierJSON;
import org.example.courier.SupportCourierMethods;
import org.junit.*;
import static org.example.Constants.SAMOKAT_URL;

// Логин курьера
public class CourierLoginTest {

    // Создаём экземпляр класса с телом запроса
    CourierJSON courierJSON;
    // Экземпляр класса с методами по курьеру
    SupportCourierMethods supportCourierMethods = new SupportCourierMethods();

    // Повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
    @Before
    public void setUp() {
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = SAMOKAT_URL;
    }

    // Достаём id курьера
    @Test
    @DisplayName("Авторизация валидиного курьера") // имя теста
    @Description("Авторизация существующего курьера (позитивный сценарий)") // описание теста
    public void getLoginCourier() {
        // Отправляем запрос на создание курьера
        supportCourierMethods.setCourierBody(new CourierJSON("Rail63", "1111"));
        supportCourierMethods.сreateCourier();
        Response response = supportCourierMethods.getLoginCourier();
        supportCourierMethods.getLoginCourierTrue(response);
    }

    // Достаём id курьера не совсеми полями (нет пароля)
    @Test
    @DisplayName("Авторизация без пароля") // имя теста
    @Description("Получение текста 'Недостаточно данных для входа' (негативный сценарий)") // описание теста
    public void getLoginCourierWithoutPassword() {
        supportCourierMethods.setCourierBody(new CourierJSON("Rail63", ""));
        Response response = supportCourierMethods.getLoginCourier();
        supportCourierMethods.getLoginCourierNotFullBody(response);
    }

    // Достаём id курьера не совсеми полями (нет логина)
    @Test
    @DisplayName("Авторизация без логина") // имя теста
    @Description("Получение текста 'Недостаточно данных для входа' (негативный сценарий)") // описание теста
    public void getLoginCourierWithoutLogin() {
        supportCourierMethods.setCourierBody(new CourierJSON("", "1111"));
        Response response = supportCourierMethods.getLoginCourier();
        supportCourierMethods.getLoginCourierNotFullBody(response);
    }

    // Достаём id не существующего курьера
    @Test
    @DisplayName("Авторизация не существующего курьера") // имя теста
    @Description("Получение текста 'Учетная запись не найдена' (негативный сценарий)") // описание теста
    public void getLoginCourierWithBugOnBody() {
        courierJSON = new CourierJSON("Rail63", "1112");
        Response response = supportCourierMethods.getLoginCourier();
        supportCourierMethods.getLoginCourierWithBugID(response);
    }

    // Удаление ранее созданных тестовых данных
    @After
    public void afterClass() throws Exception {
        supportCourierMethods.deleteCourier();
    }
}
