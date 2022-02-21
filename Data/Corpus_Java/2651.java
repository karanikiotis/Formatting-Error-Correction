package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.functions.Curried2;

import java.util.Date;

import static java.util.Calendar.MINUTE;

public class Minutes {
    public static Date add(Date date, int amount) {
        return Dates.add(date, MINUTE, amount);
    }

    public static Date subtract(Date date, int amount) {
        return Dates.subtract(date, MINUTE, amount);
    }

    public static Long between(Date start, Date end) {
        return Seconds.between(start, end) / 60;
    }

    public static class functions {
        public static Curried2<Date, Integer, Date> add = Minutes::add;

        public static Curried2<Date, Integer, Date> add()  {
            return add;
        }

        public static Curried2<Date, Integer, Date> subtract = Minutes::subtract;

        public static Curried2<Date, Integer, Date> subtract()  {
            return subtract;
        }

        public static Curried2<Date, Date, Long> between = Minutes::between;

        public static Curried2<Date, Date, Long> between()  {
            return between;
        }
    }
}