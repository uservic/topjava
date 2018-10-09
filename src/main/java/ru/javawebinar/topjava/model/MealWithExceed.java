package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealWithExceed implements Comparable<MealWithExceed> {

    private final Integer id;
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean exceed;

    public MealWithExceed(LocalDateTime dateTime, String description, int calories, int id, boolean exceed) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.exceed = exceed;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isExceed() {
        return exceed;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getCalories() {
        return calories;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserMealWithExceed{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", exceed=" + exceed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealWithExceed that = (MealWithExceed) o;
        return calories == that.calories &&
                exceed == that.exceed &&
                Objects.equals(id, that.id) &&
                Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, dateTime, description, calories, exceed);
    }

    @Override
    public int compareTo(MealWithExceed o) {
        return dateTime.compareTo(o.dateTime);
    }
}