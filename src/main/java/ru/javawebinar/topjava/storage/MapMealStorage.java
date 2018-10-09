package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapMealStorage implements Storage {
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void add(Meal meal) {
        Integer id = counter.incrementAndGet();
        meal.setId(id);
        storage.putIfAbsent(id, meal);
    }

    @Override
    public Meal getById(int id) {
        return storage.get(id);
    }

    @Override
    public void update(Meal meal) {
        Integer id = meal.getId();
        if (storage.containsKey(id)) {
            storage.put(id, meal);
        }
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }
}