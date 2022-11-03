package com.unlam.soa.tp2.entities;

public class CustomPermission {
    public CustomPermission(String name,int minSdk){
        this.name = name;
        this.minSdk = minSdk;
    }
    public String name;
    public int minSdk;
    public boolean granted;
}
