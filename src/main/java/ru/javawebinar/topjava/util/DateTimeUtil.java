package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static boolean isBetween(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        return ld.compareTo(startDate) >= 0 && ld.compareTo(endDate) <= 0;
    }

//    public static <T extends Comparable<T>> boolean isBetween(T period, T startPoint, T endPoint) {
//        return period.compareTo(startPoint) >= 0 && period.compareTo(endPoint) <= 0;
//    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate[] parseDates(String start, String end) {
        LocalDate startDate = start.equals("") ? LocalDate.MIN : LocalDate.parse(start);
        LocalDate endDate = end.equals("") ? LocalDate.MAX : LocalDate.parse(end);
        return new LocalDate[]{startDate, endDate};
    }

    public static LocalTime[] parseTimes(String start, String end) {
        LocalTime startTime = start.equals("") ? LocalTime.MIN : LocalTime.parse(start);
        LocalTime endTime = end.equals("") ? LocalTime.MAX : LocalTime.parse(end);
        return new LocalTime[]{startTime, endTime};
    }
}