package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.api.CourierJSON;
import org.example.courier.SupportCourierMethods;
import org.example.api.OrderJSON;
import org.example.order.SupportOrderMethods;
import org.junit.Test;


public class OrderListTest extends BaseTest {

    private SupportOrderMethods supportOrderMethods = new SupportOrderMethods();
    private SupportCourierMethods supportCourierMethods = new SupportCourierMethods();

    // Повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
    @Override
    public void setUp() { super.setUp(); }

    @Test
    @DisplayName("Лист заказов")
    @Description("Получение списка всех заказов курьера") // описание теста
    public void getOrderListTest() {
        // Создаём тело запроса
        supportCourierMethods.setCourierBody(new CourierJSON("Rail63", "1111", "saske"));
        // Создаём курьера
        supportCourierMethods.сreateCourier();
        // Создаём тело запроса
        supportOrderMethods.setOrderBody(new OrderJSON("Oleg",       "Dale",        "Russia",
                                                     2,              "89277773777", 5,
                                                     "2025-02-13", "Iam busy",     new String[]{"BLACK"}));
        // Создаём заказ
        Response responseCreateOrder = supportOrderMethods.сreateOrder();
        // Получаем ID курьера
        Response idCourier = supportCourierMethods.getLoginCourier();
        // Получаем ID заказа
        Response idOrder = supportOrderMethods.getIDOrder(responseCreateOrder);
        // Курьер принимает заказ по ID заказа
        supportOrderMethods.getAcceptOrder(idCourier, idOrder);
        // Получаем список заказов курьера
        Response response = supportOrderMethods.getOrderList(idCourier);
        // Проверяем, что список не пустой и статус код 200
        supportOrderMethods.checkOrderList(response);
        // Отмена заказа
        supportOrderMethods.cancelOrder(responseCreateOrder);
}

    // Удаление ранее созданного курьера
    @Override
    public void afterClass() throws Exception {
        supportCourierMethods.deleteCourier();
    }
}
