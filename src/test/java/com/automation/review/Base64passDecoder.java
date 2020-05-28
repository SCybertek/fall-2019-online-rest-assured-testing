package com.automation.review;

import com.automation.utilities.ConfigurationReader;
import org.junit.jupiter.api.BeforeAll;

import java.util.Base64;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
public class Base64passDecoder {

    @BeforeAll
    public static void beforeAll() {
        baseURI = ConfigurationReader.getProperty("SPARTAN.URI");
        authentication = basic("admin", "admin");
    }

    public static void main(String[] args) {
        byte[] decoded = Base64.getDecoder().decode("YWRtaW46YWRtaW4=");
        String value = new String(decoded);
        System.out.println(value);
    }

    //for encoding to 64
    @Test
    public void athwithEncode(){
        baseURI="http://54.146.89.247:8000";
        given()
                .baseUri(baseURI)
                .header("Authorization", "Basic "+Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                .when()
                .get("/api/spartans/search")
                .prettyPeek();
    }
}
