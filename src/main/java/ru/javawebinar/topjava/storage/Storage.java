package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {

    void addMeal(Meal meal);
    Meal getMealById(int id);
    void updateMeal(Meal meal);
    void deleteMeal(int id);
    void clear();
    int size();
    List<Meal> getAllSorted();
}