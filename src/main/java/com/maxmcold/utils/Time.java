package com.maxmcold.utils;

import com.maxmcold.logger.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class Time {
    public Time(){

    }

    public String getCurrentMonthEndFullDate(){


        LocalDate now = LocalDate.now();


        Calendar cal = Calendar.getInstance();
        int res = cal.getActualMaximum(Calendar.DATE);
        int year = now.getYear();
        Month month = now.getMonth();


        return year+"-"+month.getValue()+"-"+res;
    }

    public String getCurrentMonthStartFullDate(){
        LocalDate now = LocalDate.now();

        Month month = now.getMonth();
        int year = now.getYear();
        return year+"-"+month+"-01";
    }
    public String getMonthEndFullDate(int month){
        if (month < 1 || month >12) return 0;

        LocalDate now = LocalDate.now();
        LocalDate when = LocalDate.of(now.getYear(), Month.of(month), now.getDayOfMonth());
        return when.format();

    }
}
