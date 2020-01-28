package me.stst.weatherstation;

import java.util.TimeZone;

public class MainTest {
    public static void main(String[] args) {
        System.out.println(TimeZone.getDefault().getRawOffset()/60000);
    }
}
