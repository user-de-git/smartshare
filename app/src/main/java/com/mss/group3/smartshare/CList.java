package com.mss.group3.smartshare;

/**
 * Created by Bhupinder on 1/30/2016.
 */
public class CList {
    private int age;
    private String name;
    private String city;

    public CList(int age, String name, String city) {
        this.age = age;
        this.city = city;
        this.name = name;
    }
    public int getAge() {return this.age;};
    public String getName() {return this.name;};
    public String getCity() {return this.city;};
    public String toString() {return (name + "  " + city + "   "+age);};
}
