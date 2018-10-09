package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {

    void add(Meal meal);
    Meal getById(int id);
    void update(Meal meal);
    void delete(int id);
    List<Meal> getAll();
}