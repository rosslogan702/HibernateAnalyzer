package com.rosslogan.analyzer.model;

import javax.persistence.*;

@Entity
public class Country {
    @Id
    @Column
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

    public Country (CountryBuilder builder){
        this.code = builder.code;
        this.name = builder.name;
        this.internetUsers = builder.internetUsers;
        this.adultLiteracyRate = builder.adultLiteracyRate;
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

        return String.format("%-6s", code) + String.format("%-32s", name) + String.format("%-18s",netUsers)
                + String.format("%-18s", adultLitRate);
    }

    public static class CountryBuilder {
        private String code;
        private String name;
        private Double internetUsers;
        private Double adultLiteracyRate;

        public CountryBuilder(String code, String name){
            this.code = code;
            this.name = name;
        }

        public CountryBuilder withInternetUsers(Double internetUsers){
            this.internetUsers = internetUsers;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate(Double adultLiteracyRate){
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }

        public Country build (){
            return new Country(this);
        }
    }

}
