package me.stst.weatherstation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.TimeZone;

public class MainTest {
    public static void main(String[] args) {
        System.out.println(TimeZone.getDefault().getRawOffset()/60000);
        System.out.println((5-1)/2);
        System.out.println(BigDecimal.valueOf(2).divide(BigDecimal.valueOf(3),4, RoundingMode.HALF_UP));
    }
}
