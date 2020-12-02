package com.example.id_qr.data_models;

public class User {

    private String firstName;
    private String lastName;
    private int edad;

    public User() {
        //Required public constructor
    }

    public User(String firstName, String lastName, int edad) {
        this.edad = edad;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
