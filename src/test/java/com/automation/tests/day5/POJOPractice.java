package com.automation.tests.day5;

import com.automation.pojos.Spartan;
import com.automation.utilities.ConfigurationReader;
import com.google.gson.Gson;
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

public class POJOPractice {


    //setup
    @BeforeAll
    public static void beforeAll(){
        baseURI = ConfigurationReader.getProperty("SPARTAN.URI");
    }

    @Test
    public void getUser(){
        Response response =   given().
                auth().
                basic("admin", "admin").
                when().
                get("/spartans/{id}", 393).prettyPeek();
        //saving as java object : deserialization
        // Get the body and map it to a Java object.
        // For JSON responses this requires that you have either Jackson or Gson
        //this is a deserialization
        Spartan spartan = response.as(Spartan.class); //this is .as coming from GSon
        System.out.println(spartan);
        //spartan class is more specific
        //easier to modify the data / easier to parse.
        //there are nested JSON objects

        assertEquals(393, spartan.getId());
        assertEquals("Michael Scott", spartan.getName());


        //different way of storing our response :
        //using Map : keys will be property names

        //deserialization: POJO <- JSON
        //serialization:   POJO -> JSON

        //both operations are done with a help of Gson.
        //RestAssured automatically calls GSon for these operations
        //any JSON object can be stored in Map object.

        Map<String, ? > spartanAsMap = response.as(Map.class);
        System.out.println(spartanAsMap);
    }

    @Test
    public void addUser(){

        Spartan spartan = new Spartan("Hasan Jan", "Male", 3124576589L);
        Gson gson = new Gson();
        String pojoAsJson = gson.toJson(spartan);
        System.out.println(pojoAsJson);

        Response response = given().
                auth().basic("admin", "admin").
                contentType(ContentType.JSON). //what kind of media we are sending to the server
                body(spartan).
                when().post("/spartans").prettyPeek(); //using which extension basically

    int userId = response.jsonPath().getInt("data.id"); //data is our collection name in response
        System.out.println("user Id = " + userId);

        //to delete
        System.out.println("######DELETE USER#####");

        given().auth().basic("admin", "admin").
                when().delete("/spartans/{id}",userId).prettyPeek().
                then().assertThat().statusCode(204);//to ensure that user was deleted

    }







}
