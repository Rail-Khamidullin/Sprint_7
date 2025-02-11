package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.api.OrderJSON;
import org.example.order.SupportOrderMethods;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.example.Constants.SAMOKAT_URL;

// Создание заказа
@RunWith(Parameterized.class)
public class OrderTest {

    SupportOrderMethods supportOrderMethods = new SupportOrderMethods();

    private String firstName = "Oleg";
    private String lastName = "Olegovich";
    private String address = "Свободы, 82";
    private int metroStation = 2;
    private String phone = "+7 927 333 33 33";
    private int rentTime = 5;
    private String deliveryDate = "2025-02-05";
    private String comment = "Sprint_7";
    private String[] color;

    public OrderTest(String[] color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = SAMOKAT_URL;
    }

    // Проверим работу класса с разными входными данными
    @Parameterized.Parameters
    public static Object[][] colorOrder() {
        return new Object[][]{
                {new String[] {"BLACK"}},
                {new String[] {"GREY"}},
                {new String[] {"BLACK", "GREY"}},
                {new String[] {}}
        };
    }

    @Test
    @DisplayName("Создание заказа с цветом" + "Arrays.toString(color)")
    @Description("Проверяем создание заказа с разными цветами")
    public void createOrderTest() {
        supportOrderMethods.setOrderBody(new OrderJSON(
                firstName,    lastName, address,
                metroStation, phone,    rentTime,
                deliveryDate, comment,  color));
        Response response = supportOrderMethods.сreateOrder();
        // Получаем ответ с номером заказа и статус кодом 201
        supportOrderMethods.createOrderResponse(response);
        System.out.println(Arrays.toString(color));
        // Удаление созданного заказа
        supportOrderMethods.cancelOrder(response);
    }
}
