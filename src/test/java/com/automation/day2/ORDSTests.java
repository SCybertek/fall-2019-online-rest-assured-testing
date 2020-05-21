package com.automation.day2;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
//we are having this imported as static

public class ORDSTests {

    String BASE_URL = "http://54.209.240.192:1000/ords/hr";


    @Test
    @DisplayName("Get list of all employees")
    //this will help t provide more detailed description of test
    public void getAllEmployees() {
        //response can be saved in the Response object
        //prettyPeek() - method that prints response info in nice format
        //also ths method returns Response object
        //response contains body, header and status line
        //body (payload) - contains content that we requested from the web service
        //header - contains meta data
        Response response = given().
                baseUri(BASE_URL).
                when().
                get("/employees").prettyPeek();

        //pretty Print will not allow to safe your result in Response
        //below information on console ; is coming from prettyPeek() (as metadata.) In postman we can see this info in header!
//HTTP/1.1 200 OK
//Date: Sun, 17 May 2020 17:49:37 GMT
//Content-Type: application/json
//ETag: "xfpAm9x57e33U1Dl8yyjlpmQLl+Qna+crKCi0D3uC411jNhiCvtge/43GaRp/QDxcL8maPYmmoosDvJywG+M5A=="
//Transfer-Encoding: chunked

        /**
         * RestAssured request has similar structure to BDD scenarios:
         * Start building the request part of the test
         *
         * given() - used for request setup and authentication
         * Syntactic sugar,
         * when() - to specify type of HTTP request: get, put, post, delete, patch, head, etc...
         * then() - to verify response, perform assertions
         */


    }


    @Test
    @DisplayName("Get employee under specific ID")
    public void getOnEmployee() {
        //in URL we can specify path and query parameters
        //path parameters are used to retrieve specific resource: for example 1 employee not all of them
        //{id} - path variable, that will be replace with a value after comma
        //after when() we specify HTTP request type/method/verb
        //The path parameters. E.g. if path is "/book/{hotelId}/{roomNumber}" you can do <code>get("/book/{hotelName}/{roomNumber}", "Hotels R Us", 22);</code>.
        Response response = given().baseUri(BASE_URL).when().get("/employees/{id}", 100).prettyPeek();
        //below did not work ..why ? because our end point doe snot have search by name
        //Response response2 = given().baseUri(BASE_URL).when().get("/employees/?name=Steven").prettyPeek();

        //how we verify response? - use assertions
        response.then().statusCode(200); // to verify that status code is equals to 200

        //if we change it to 201 : we get AssertionError
        int statusCode = response.statusCode(); // to save status code in variable

        Assertions.assertEquals(200, statusCode);
        //if assertions fails, you will get this kind of message:
        /**
         * java.lang.AssertionError: 1 expectation failed.
         * Expected status code <201> but was <200>.
         * 200 is always expected status code after GET requset
         */
    }

    //when our test passes  we prove it from html reports rigtht?
    //depends:
    //some has HTML some PDF.. but MOST use HTML like in cucumber to prove test results

//COPY!!!! from github

    /**
     * given base URI = http://3.90.112.152:1000/ords/hr
     * when user sends get request to "/countries"
     * then user verifies that status code is 200
     */
    @Test
    @DisplayName("Get countries")
    public void getCountries() {
        String base_url = "http://3.90.112.152:1000/ords/hr";
        Response response = given().baseUri(BASE_URL).when().get("/countries").prettyPeek();
        response.then().statusCode(200).statusLine("HTTP/1.1 200 OK");
        //no need to create response object if we do not need that object

        ///statusLine - to verify status line

    }
}
