package com.automation.tests.day4;

import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class WarmUPTask {

    /**
     * Warmup!
     * Given accept type is JSON
     * When users sends a GET request to “/employees”
     * Then status code is 200
     * And Content type is application/json
     * And response time is less than 3 seconds
     */

    //baseURI="http://54.224.118.38:1000/ords/hr"; => Vasyl's

    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("ORDS_URI");

        //we can also move it to properties file
        //this provides ease if server dies and we need to update very class etc.
        //also, sometimes it works in new tests but in old tests if server was different it does not work
        //to prevent that we can create properties file

        //for entrire framework we can have more than 1 properties

    }
    @Test
    public void warmUp(){
        //flow:
        //pretty peek goes after request
        given().accept(ContentType.JSON).
                when().
                get("/employees").prettyPeek().
                then().
                assertThat().
                header("Content-Type", "application/json").
                statusCode(200).contentType(ContentType.JSON).
                time(lessThan(3L), TimeUnit.SECONDS);
    }

    //below are the same:
//only thing is in second one we have to know which exactly the content-type
//contentType(ContentType.JSON)
//header("Contet-TYpe","appocation/json").

    /**
     *
     Given accept type is JSON
     And parameters: q = {"country_id":"US"}
     When users sends a GET request to "/countries"
     Then status code is 200
     And Content type is application/json
     *
     */
@Test
@DisplayName("Verify country name, content type and status code for country with ID US")
    public void verifyCountriesTest1() {
    given().accept(ContentType.JSON).
            queryParam("q", "{\"country_id\":\"US\"}").
            when().get("/countries").//prettyPeek().
            then().assertThat().
            statusCode(200).
            contentType(ContentType.JSON).
            body("items[0].country_name", is("United States of America"));
//no need to create separate Json path object in here


    //second request
    Response response = given().accept(ContentType.JSON).
            when().get("/countries").prettyPeek();

    //response.path()=> generic path = rest assured will determine
    //ours we know that is Jsonpath thats why we use JsonPath
    //items is collection name
    //grovy path : find()method from groovy : will return Object
    //JasonPath is used to get something inside response body //<it> predicate .that will represent collection and will check country_id
    String countryName = response.jsonPath().getString("items.find{it.country_id=='US'}.country_name");
    //saving country name as an object in Map

    Map<String, Object> countryUS = response.jsonPath().get("items.find{it.country_id=='US'}");
    //collections of Objects : findAll()
    List<String> countryNames = response.jsonPath().getList("items.findAll{it.region_id ==2}.country_name");
    //find all country names from region 2
    //collectionName.findAll{it.propertyName == 'Value'} -- to get collection objects where property equals to some value
    //collectionName.find{it.propertyName == 'Value'} -- to object where property equals to some value

    // to get collection properties where property equals to some value
    //collectionName.findAll{it.propertyName == 'Value'}.propertyName

    //the point of strong country in Map .. it will store object ..entire info
    System.out.println("countryName = " + countryName);
    System.out.println(countryUS);
    System.out.println("countryNames = " + countryNames);

    for (Map.Entry<String, Object> entry : countryUS.entrySet()) {
        System.out.printf("key= %s, value = %s\n", entry.getKey(), entry.getValue());
    }
}
    //let's find employee with highest salary. Use GPath

    @Test
    public void getEmployeesTest(){
        Response response = when().get("/employees").prettyPeek();
        //collectionName.max{it.propertyName}
        Map<String, ? > bestEmployee = response.jsonPath().get("items.max{it.salary}");
                                                            //items is collection name..if no collectin name : $ or .
        Map<String, ? > poorGuy = response.jsonPath().get("items.min{it.salary}");
        int companysPayroll = response.jsonPath().get("items.collect{it.salary}.sum()");

        System.out.println(bestEmployee);
        System.out.println("poorGuy = " + poorGuy);
        System.out.println("Company's payroll: " + companysPayroll);

        //api and SQL result may vary in our case
    }

    /**
     * given path parameter is “/employees”
     * when user makes get request
     * then assert that status code is 200
     * Then user verifies that every employee has positive salary
     *
     */

    @Test
    @DisplayName("Verify that every employee has positive salary")
    public void testSalary(){
        when().get("/employees").prettyPeek().
                then().assertThat().statusCode(200).
                 body("items.salary", everyItem(greaterThan(0))).
                log().ifError();
    }

    /**
     * given path parameter is “/employees/{id}”
     * and path parameter is 101
     * when user makes get request
     * then assert that status code is 200
     * and verifies that phone number is 515-123-4568
     *
     */
    @Test
    @DisplayName("Verify that phone number is 515-123-4568")
    public void testPhoneNumber(){
        Response response = given().get("/employees/{id}",101).prettyPeek();
            response.then().assertThat().statusCode(200);

          String expected = "515-123-4568";
          String actual = response.jsonPath().getString("phone_number").replace(".", "-");

          assertEquals(200, response.statusCode());
          assertEquals(expected, actual)

          ;
                //don't be afraid of failed test
                //sometimes if you use soft assertion then
                //he was assigning it to BA all the time.. so bug is not lost..do not assign to developers.. product owner will say if the bug is high priority or it can be deferred to some other spring

    }
}
//API  :it can be separate manual API and automation API tester
// manual testers of API also document their work that they do on postman
// it can be done in regular Microsoft Doc file .. you take screen shot..steps etc
//and on JIRA you can add bug reports




