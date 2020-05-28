package com.automation.tests.day8;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class BearerAuthentication {

    //BOOKIT application :


    @BeforeAll
    public static void setup(){
        baseURI = "https://cybertek-reservation-api-qa.herokuapp.com/";
    }

    @Test
    public void loginTest(){
        Response response = given().
                queryParam("email", "teacherva5@gmail.com").
                queryParam("password", "maxpayne").
                when().
                get("/sign").prettyPeek();

        String token = response.jsonPath().getString("accessToken");
        System.out.println("Token :: " + token);
    }

    @Test
    @DisplayName("Negative test: attempt to retrieve list of rooms without authentication token")
    public void getRoomsTest() {
        //422 ok. because anyways we didn't get data
        //but, we supposed to get 401 status code
        get("/api/rooms").prettyPeek().then().statusCode(401);
    }

    //first request below is to retrieve OUR BEARER TOKEN (authentication)
    @Test
    public void getRoomsTest2(){
        Response response = given().
                queryParam("email", "teacherva5@gmail.com").
                queryParam("password", "maxpayne").
                when().get("/sign");
        response.then().log().ifError();

        String token = response.jsonPath().getString("accessToken");

        Response response2 = given().
                auth().oauth2(token).
                when().
                get("/api/rooms").prettyPeek();
    }

    @Test
    public void getAllTeamsTest(){
        Response response = given().
                header("Authorization", "Bearer "+getToken()).
                when().
                get("/api/teams").prettyPeek();

        response.then().statusCode(200);
    }

    //Alternative way, for one who prefer it this way
    //@Test
    //    public void getAllTeamsTest(){
    //        Response response =
    //        given().
    //                auth().oauth2(getToken("teacherva5@gmail.com","maxpayne")).
    //        when().
    //                get("/api/teams").prettyPeek();
    //        response.then().statusCode(200);
    //    }


    /**
     *JWT token expires (continuous case for now .. in real life always )
     * @return returns token
     * for easy regeneration of the token, when it expires
     */
    public String getToken() {
        Response response = given().
                queryParam("email", "teacherva5@gmail.com").
                queryParam("password", "maxpayne").
                when().
                get("/sign");
        response.then().log().ifError();
        String token = response.jsonPath().getString("accessToken");
        System.out.println("Token :: " + token);
        return token;
    }

    /**
     * Overloaded method of the above
     * @param email
     * @param password
     * @return
     */
    public String getToken(String email, String password) {
        Response response = given().
                queryParam("email", email).
                queryParam("password", password).
                when().
                get("/sign");
        response.then().log().ifError();

        String token = response.jsonPath().getString("accessToken");
        System.out.println("Token :: " + token);
        return token;
    }






}
