package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.api.CourierJSON;
import org.example.courier.SupportCourierMethods;
import org.example.api.OrderJSON;
import org.example.order.SupportOrderMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.example.Constants.SAMOKAT_URL;

public class OrderListTest {

    private SupportOrderMethods supportOrderMethods = new SupportOrderMethods();
    private SupportCourierMethods supportCourierMethods = new SupportCourierMethods();

    // Повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
    @Before
    public void setUp() {
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = SAMOKAT_URL;
    }

    @Test
    @DisplayName("Лист заказов")
    @Description("Получение списка всех заказов курьера") // описание теста
    public void getOrderListTest() {

        // Создаём тело запроса
        supportCourierMethods.setCourierBody(new CourierJSON("Rail1666", "2666", "saske"));
        // Создаём курьера
        supportCourierMethods.сreateCourier();
        // Создаём тело запроса
        supportOrderMethods.setOrderBody(new OrderJSON("Oleg", "Dale", "Russia",
                                                     2, "89277773777", 5,
                                                     "2025-02-13", "Iam busy", new String[]{"BLACK"}));
        // Создаём заказ
        Response responseCreateOrder = supportOrderMethods.сreateOrder();
        // Получаем ID курьера
        Response idCourier = supportCourierMethods.getLoginCourier();
        // Получаем ID заказа
        Response idOrder = supportOrderMethods.getIDOrder(responseCreateOrder);
        // Курьер принимает заказ по ID заказа
        supportOrderMethods.getAcceptOrder(idCourier, idOrder);
        // Получаем список заказов курьера
        Response response = supportOrderMethods.getOrderList();
        // Проверяем, что список не пустой и статус код 200
        supportOrderMethods.checkOrderList(response);
}

    // Удаление ранее созданных тестовых данных
    @After
    public void afterClass() throws Exception {
        supportCourierMethods.deleteCourier();
    }
}
