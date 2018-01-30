package com.rosslogan.analyzer.model;

import javax.persistence.*;

@Entity
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;
    @Column
    private String name;
    @Column
    private Double internetUsers;
    @Column
    private Double adultLiteracyRate;

    //Default constructors for JPA
    public Country(){

    }

    public Country(String code, String name, Double internetUsers, Double adultLiteracyRate) {
        this.code = code;
        this.name = name;
        this.internetUsers = internetUsers;
        this.adultLiteracyRate = adultLiteracyRate;
    }

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

    @Override
    public String toString() {
        String netUsers = internetUsers==null ? "--" :
                String.format("%.02f", Math.round(internetUsers * 100.0)/100.0);
        String adultLitRate = adultLiteracyRate==null ? "--" :
                String.format("%.02f",Math.round(adultLiteracyRate * 100.0)/100.0);

        return String.format("%-32s", name) + String.format("%-18s",netUsers) + String.format("%-18s", adultLitRate);
    }

}
