package com.automation.tests.day6;


import com.automation.pojos.Spartan;
import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.*; //for baseURI
import static org.hamcrest.Matchers.*;


public class POJOPracticeWithSpartanApp {

    //setup
    @BeforeAll
    public static void beforeAll(){
        baseURI = ConfigurationReader.getProperty("SPARTAN.URI");
        authentication = basic("admin", "admin");
    }

    @Test
    public void addSpartanTest(){
        /**
         * {
         *   "gender": "Male",
         *   "name": "Nursultan",
         *   "phone": "123112312312"
         * }
         */
        //how to represent a data if we want to add a new user ?
        //we have POJO for spartan right now => this is NOT the only way to add user using POST or PUT request
        //BUT we can also use MAP:
        Map<String, Object> spartan  = new HashMap<>();
        spartan.put("gender", "Male");
        spartan.put("name", "Nursultan");
        spartan.put("phone", "1234567890");
        //if we change phone to phoneNumber : 400 Bad request status code because it does not match the same parameter in our server

        //this translation is done by gson library : from map to json

        RequestSpecification requestSpecification = given().
                auth().basic("admin", "admin").
                contentType(ContentType.JSON).
                accept(ContentType.JSON).body(spartan);

        Response response = given().
                auth().basic("admin", "admin").
                contentType(ContentType.JSON).body(spartan).
                when().post("/spartans").prettyPeek();
        // post request because we are posting new spartan to server

        //now assert  :

        response.then().statusCode(201);
        //to verify something in body
        response.then().body("success", is("A Spartan is Born!")); //this body is JSON path (key value)

        //using jsonpath to extract values from the response :and we are saying that this Object belongs to SPartan class
        //bellow is deserialization
        Spartan spartanResponse = response.jsonPath().getObject("data", Spartan.class);

        //checking if spartanResponse is a Spartan
        System.out.println(spartanResponse instanceof Spartan); //must be true
        //or spartanResponse != null
    }

    @Test
    public void updateSpartanTest(){

        int userToUpdate = 101;
        String name = "Maya";

        //HTTP PUT request to provide existing record, for example existing spartan
        //PUT requires to provide ALL parameters in BODY

        Spartan spartan = new Spartan(name, "Female", 1234567890);

        //instead of creating Spartan object over and over why not :
        //so in here we do not need to enter all params for spartan object!

        Spartan spartanToUpdate = given().
                                    auth().basic("admin", "admin").
                                    accept(ContentType.JSON).
                                when().
                                    get("/spartans/{id}", userToUpdate).as(Spartan.class);
        //update property that you need without affecting other properties :
        System.out.println("Before update : " + spartanToUpdate);
        //we have setter to change ONLY name ! other params are the same
        spartanToUpdate.setName(name);
        System.out.println("After update : " + spartanToUpdate);

        //request to update existing user with id 101
        Response response = given().
                                    contentType(ContentType.JSON).
               // accept(ContentType.JSON). not needed in here
                                    auth().basic("admin", "admin").
                                    body(spartan).
                            when().
                        put("/spartans/{id}", userToUpdate).prettyPeek();

        //verify that status code is 204
        response.then().statusCode(204);

        System.out.println("#########################");

        //to get user that we just updated :
        given().
                auth().basic("admin", "admin").
        when().
                get("/spartans/{id}", userToUpdate).prettyPeek().
        then().
                statusCode(200).body("name", is(name));
    }
}
