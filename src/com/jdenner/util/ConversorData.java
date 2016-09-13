package com.jdenner.util;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class ConversorData {

    public static Date converter(LocalDate localDate) {
        Calendar cal = Calendar.getInstance();
        cal.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        return cal.getTime();
    }

    public static LocalDate converter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

}
