package com.automation.pojos;

// 1-step - we need to create Java class that has THE SAME (corresponding) property of our JSON object
// so you have to read your Json file first? Gson can't fill in the key  values automatically?
// yes, exactly!
// if for SOME reason it is different : we need to map them using @SerializedName : will change property name during serialization .
// and it will help to have diff name
// {
//	“id”: 393,
//	“name”: “Michael Scott”,
//	“gender”: “Male”,
//	“phone”: 6969696969
//}

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Spartan {

    private int id;
    private String name;
    private String gender;
    @SerializedName("phone")
    private long phoneNumber;
    //if name is not matching the data will be NULL


    public Spartan(String name, String gender, long phoneNumber ) {
        this.name = name;
        this.gender = gender;
        setPhoneNumber(phoneNumber);
        //id is not there , because ID cannot be specified when you create a post request.
        //it is autogenerated by the server
    }

    //not required for POJO as well :
    public Spartan (){

    }

    //these constructor is optional.. POJO does not require it
    //we have these extra constructor for our convinience
    public Spartan(int id, String name, String gender, long phoneNumber) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        setPhoneNumber(phoneNumber);
    }

    //we do not need getters and setters in POJO
    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        if (String.valueOf(phoneNumber).length() < 10) {
            throw new RuntimeException("Phone number is too short!");
        }
        this.phoneNumber = phoneNumber;

    }

    //human readable object representation
    @Override
    public String toString() {
        return "Spartan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }

    //compares if 2 reference variables are pointing on the same object
    //if 2 objects are located in the same place
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Spartan)) return false;
        Spartan spartan = (Spartan) o;
        return id == spartan.id;

//                phoneNumber == spartan.phoneNumber &&
//                Objects.equals(name, spartan.name) &&
//                Objects.equals(gender, spartan.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gender, phoneNumber);
    }
    //hashMap or hashSet they all use this hash numbers.
    //HashSet is using hashcode to decide if object is the same or not
}
