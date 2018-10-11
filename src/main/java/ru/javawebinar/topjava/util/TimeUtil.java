package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static LocalDateTime parseToLocalDateTime(String s) {
        return LocalDateTime.parse(s.trim().replaceFirst(" ", "T"), FORMATTER);
    }

    public static String parseToString(LocalDateTime dateTime) {
        return FORMATTER.format(dateTime).replace('T', ' ');
    }
}