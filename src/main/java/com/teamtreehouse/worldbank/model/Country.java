package com.teamtreehouse.worldbank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Country {

    @Id
    private String code;
    @Column
    private String name;
    @Column
    private Double internetUsers;
    @Column
    private Double adultLiteracyRate;


    // Default constructor for JPA
    public Country(){}


    // #######  Getters and Setters ######

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(double internetUsers) {
        this.internetUsers = internetUsers;
    }

    public Double getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(double adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }
}
