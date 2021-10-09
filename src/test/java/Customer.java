import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Customer {
    Properties properties = new Properties();
    FileInputStream file;

    {
        try {
            file = new FileInputStream("./src/test/resources/config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String token;

    public void CallingLoginApi() throws IOException, ConfigurationException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseUrl");
        Response response = given()
                .contentType("application/json")
                .body("{\"username\":\"salman\",\n" +
                        "    \"password\":\"salman1234\"}")
                .when()
                .post("/customer/api/v1/login")
                .then()
                .assertThat().statusCode(200).extract().response();
        System.out.println(response.asString());
        JsonPath jsonPath = response.jsonPath();
        token = jsonPath.get("token");
        Utils.SetEnvironmentVariable("token", token);
    }

    public void CustomerList() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseUrl");
        Response res = given()
                .contentType("application/json").header("Authorization", properties.getProperty("token"))
                .when()
                .get("/customer/api/v1/list")
                .then()
                .assertThat().statusCode(200).extract().response();
        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("Customers[0].id").toString(), "1117");
        System.out.println(res.asString());
    }

    public void CreateCustomer() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseUrl");
//        JSONObject jsonObj = new JSONObject(IOUtils.toString(new URL("https://randomuser.me/api/"), Charset.forName("UTF-8")));
//        String name = (String) jsonObj.get("name.first" + " " + "name.last");
//        String email = (String) jsonObj.get("email");
//        String address = (String) jsonObj.get("location.street.name");
//        String phone_number = (String) jsonObj.get("phone");
        Response res = given()
                .contentType("application/json").header("Authorization", properties.getProperty("token"))
                .body("{\n" +
                        "    \"id\": 509,\n" +
                        "    \"name\": \"Pollab\",\n" +
                        "    \"email\": \"pollab@mail.com\",\n" +
                        "    \"address\": \"Dhaka\",\n" +
                        "    \"phone_number\": \"01948563837\"\n" +
                        "}")
                .when()
                .post("/customer/api/v1/create")
                .then()
                .assertThat().statusCode(201).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString(), "Success");
        System.out.println(res.asString());
    }

    public void SearchCustomer() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseUrl");
        Response res = given()
                .contentType("application/json").header("Authorization", properties.getProperty("token"))
                .when()
                .get("/customer/api/v1/get/509")
                .then()
                .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("name").toString(), "Pollab");
        System.out.println(res.asString());
    }

    public void UpdateCustomer() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseUrl");
        Response res = given()
                .contentType("application/json").header("Authorization", properties.getProperty("token"))
                .body("{\n" +
                        "    \"id\":509,\n" +
                        "    \"name\":\"Rahim Uddin\",\n" +
                        "    \"email\":\"rahim@test.com\",\n" +
                        "    \"address\":\"Gulshan, Dhaka\",\n" +
                        "    \"phone_number\":\"01502212471\"\n" +
                        "}")
                .when()
                .put("/customer/api/v1/update/509")
                .then()
                .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString(), "Success");
        System.out.println(res.asString());
    }

    public void DeleteCustomer() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseUrl");
        Response res = given()
                .contentType("application/json").header("Authorization", properties.getProperty("token"))
                .when()
                .delete("/customer/api/v1/delete/509")
                .then()
                .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString(), "Customer deleted!");
        System.out.println(res.asString());
    }
}
