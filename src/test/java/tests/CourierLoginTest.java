package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.api.CourierJSON;
import org.example.courier.SupportCourierMethods;
import org.junit.*;


// Логин курьера
public class CourierLoginTest extends BaseTest {

    // Экземпляр класса с методами по курьеру
    final SupportCourierMethods supportCourierMethods = new SupportCourierMethods();

    // Повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
    @Override
    public void setUp() { super.setUp(); }

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
        supportCourierMethods.setCourierBody(new CourierJSON("AbraCadabra63jhgh", "1111"));
        Response response = supportCourierMethods.getLoginCourier();
        supportCourierMethods.getLoginCourierWithBugID(response);
    }

    // Удаление ранее созданных тестовых данных
    @Override
    public void afterClass() throws Exception {
        supportCourierMethods.deleteCourier();
    }
}
