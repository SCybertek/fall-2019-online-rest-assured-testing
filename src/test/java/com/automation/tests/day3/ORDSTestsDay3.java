package com.automation.tests.day3;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ORDSTestsDay3 {

    @BeforeAll
    public static void setup(){
        baseURI = "http://54.209.240.192:1000/ords/hr";
    }
    /**
     * given path parameter is "/regions/{id}"
     * when user makes get request
     * and region id is equals to 1
     * then assert that status code is 200
     * and assert that region name is Europe
     */

    @Test
    public void verifyFirstRegion(){
        given().
                pathParam("id", 1). //parameter
                when().
                get("/regions/{id}").prettyPeek(). //this is our end point
                then().
                assertThat().statusCode(200).
                body("region_name",is("Europe")).
                body("region_id",is(1)).time(lessThan(5L), TimeUnit.SECONDS); //verify that response time is less than 5 seconds

        //is it like performance test ? specifying time ?
        //JMETER simuates from the 50 or 100 users and compare metrics how application was behaving when 50 users were using this app or only
    }

    //our list of employees is Array. because it prints like :"items": [
    @Test
    public void verifyEmployee(){       //content type is Enum
                                    //content type : is for sending and specifying what data you are sending
                                    //accept file from the server back get data back
        Response response = given().
                accept(ContentType.JSON). //this support MUST be provided by developers. if developers DO NOT set it up then it will not return for example
                when().
                get("/employees");
             /**
              *  * Groovy <a href="http://docs.groovy-lang.org/latest/html/documentation/#_gpath">GPath</a>
                *  syntax when getting an object from the document. You can regard it as an alternative to XPath for JSON.
                */

        //items - name of the array where all employees are stored
        //GPath, something like Xpath but different. Gpath use Groovy syntax
        JsonPath jsonPath = response.jsonPath(); //Json path is alrenative from Xpath and follows groovy syntax
        String nameOfFirstEmployee = jsonPath.getString("items[0].first_name"); // 0 to get first employee
        String nameOFLastEmployee = jsonPath.getString("items[-1].first_name");  // -1 to get first employee

        System.out.println("nameOfFirstEmployee = " + nameOfFirstEmployee);
        System.out.println("nameOFLastEMployee = " + nameOFLastEmployee);

        //saving inside the map
        //string is key name and value is object
        //we can also put ? instead of Object
        Map<String, ?> firstEmployee = jsonPath.get("items[0]");
        System.out.println("firstEmployee = " + firstEmployee);

    }

////Groovy is a programming language, based on java, some libraries in rest assures comes from groovy

}
