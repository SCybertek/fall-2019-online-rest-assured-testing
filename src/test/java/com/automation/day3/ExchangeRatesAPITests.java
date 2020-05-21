package com.automation.day3;


import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class ExchangeRatesAPITests {

    /**
     * <p>{@code @BeforeAll} methods must have a {@code void} return type,
     *  * must not be {@code private}, and must be {@code static} by default.
     */
    @BeforeAll
    public static void setUp(){ //because we want it to belong to the class
        //without static import
        //RestAssured.baseURI

        //for every single request this is pour base URI
        baseURI = "http://api.openrates.io"; // API itself no endpoints in here right now
    }

    //no need for AfterAll for us

    @Test
    public void getLatestRates(){
        Response response = get("/latest").prettyPeek();
        //verify that this responce was successful
        response.then().assertThat().statusCode(200);
        //contentype => is for postRequest

        //When to use  prettyPeek  and prettyPrint ?
        //prettyPeek returns response Object
        //prettyPrint returns response as String
    }

    @Test
    public void getLatestRatesUSD() {
        // after ? we specify query parameters. If there are couple of them we use & to concatenate them
        //http://www.google.com/index.html?q=apple&zip=123123
        //q - query parameter
        //zip - another query parameter
        //with rest assured, we provide query parameters into given() part.
        //give() - request preparation
        //you can specify query parameters in URL explicitly: http://api.openrates.io/latest?base=USD
        //rest assured, will just assemble URL for you

        Response response = given().queryParam("base", "USD").
                when().get("/latest").prettyPeek();



        //to read header of the response
        Headers headers = response.getHeaders();
        String contentType = headers.getValue("Content-Type");
        System.out.println("contentType = " + contentType);


        //verify that GET request to the endpoint was successful
        response.then().statusCode(200);                //is or equalsTo
        response.then().assertThat().body("base", is("USD")); //is => Matchers.is("EUR")

        //to verify date :
        //response.then().assertThat().body("date", containsString("2020-05-19"));

        //to verify todays date : copy!!!
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd") );
        response.then().assertThat().body("date",containsString(date) );

        // this is another way / without Hemcrest:
        // assertTrue(response.body().asString().contains("USD"));
        //how to verify if base exists? how to verify body ?
        //we use hemcrest matcher:it is fancy assertion
        //it was created for unit testing originally
    }

    //get history of rates for 2008

    @Test
    public void getHistoryOfRates(){
        Response response = given().
                queryParam("base","USD").
                when().
                get("/2008-01-02").prettyPeek();
        Headers headers = response.getHeaders(); //response header
        System.out.println(headers);
        response.then().assertThat().
                statusCode(200).
                and().
                body("date",is("2008-01-02")).
                and().
                body("rates.EUR", is(0.6808278867f) );

        
        //reading BODY : 
        //rates is an Object in body (not array)
        //all currency are like instance variable
        //to get any instance variable(property), objectName.propertyName

        //JSON has data types as well
        //properties are ALWAYS String .. BUT value can be different
        
        float parameter = response.jsonPath().get("rates.EUR");
        System.out.println("parameter = " + parameter);

        float actual = response.jsonPath().get("rates.EUR");
        assertEquals(0.6808278867f, actual);
        System.out.println("actual = " + actual);

        // response.then().assertThat().
        //                                statusCode(200).
        //                            and().
        //                                body("date", is("2008-01-02")).
        //                            and().
        //                                body("rates.USD", is(1.0f));
        //        //and() doesn't have a functional role, it's just a syntax sugar
        //        //we can chain validations
        //        //how we can retrieve
        //        //rates - it's an object
        //        //all currencies are like instance variables
        //        //to get any instance variable (property), objectName.propertyName
//        float actual = response.jsonPath().get("rates.USD");
//        assertEquals(1.0, actual);

    }

    /**
     *  Get a JsonPath view of the response body. This will let you use the JsonPath syntax to get values from the response.
     *      * Example:
     *      * <p>
     *      * Assume that the GET request (to <tt>http://localhost:8080/lotto</tt>) returns JSON as:
     *      * <pre>
     *      * {
     *      * "lotto":{
     *      *   "lottoId":5,
     *      *   "winning-numbers":[2,45,34,23,7,5,3],
     *      *   "winners":[{
     *      *     "winnerId":23,
     *      *     "numbers":[2,45,34,23,3,5]
     *      *   },{
     *      *     "winnerId":54,
     *      *     "numbers":[52,3,12,11,18,22]
     *      *   }]
     *      *  }
     *      * }
     *      * </pre>
     *      * </p>
     *      * You can the make the request and get the winner id's by using JsonPath:
     *      * <pre>
     *      * List<Integer> winnerIds = get("/lotto").jsonPath().getList("lotto.winnders.winnerId");
     *      * </pre>
     *
     */
}
//syntatic sugar : given() when() words.. test will work without them as well
