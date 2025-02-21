package tests;

import io.restassured.RestAssured;
import org.example.courier.SupportCourierMethods;
import org.junit.After;
import org.junit.Before;

import static org.example.Constants.SAMOKAT_URL;

public class BaseTest {

    private SupportCourierMethods supportCourierMethods = new SupportCourierMethods();

    // Повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
    @Before
    public void setUp() { RestAssured.baseURI = SAMOKAT_URL; }

    // Удаление ранее созданного курьера
    @After
    public void afterClass() throws Exception {
        supportCourierMethods.deleteCourier();
    }
}
