package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapMealStorage implements Storage {
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void addMeal(Meal meal) {
        Integer id = counter.incrementAndGet();
        meal.setId(id);
        storage.put(id, meal);
    }

    @Override
    public Meal getMealById(int id) {
        return storage.get(id);
    }

    @Override
    public void updateMeal(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public void deleteMeal(int id) {
        storage.remove(id);
        counter.decrementAndGet();
    }

    @Override
    public void clear() {
        storage.clear();
        counter.set(0);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public List<Meal> getAllSorted() {
        List<Meal> result = new ArrayList<>(storage.values());
        Collections.sort(result);
        return result;
    }
}