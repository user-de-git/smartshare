package com.mss.group3.smartshare.common;

import java.util.ArrayList;


public class User {
    public static ArrayList<String> vehicle_list = new ArrayList();

    public static ArrayList<String> getCar_list() {
        return vehicle_list;
    }

    public static void addVehicle(String car_plate) {
        if(vehicle_list.contains(car_plate)) return;
        vehicle_list.add(car_plate);
    }
}
