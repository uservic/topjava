package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private static final Integer AUTH_USER = 1;

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, AUTH_USER));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            Integer id = counter.incrementAndGet();
            meal.setId(id);
            meal.setUserId(userId);
            repository.put(id, meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return checkIfBelongsToAuthUser(meal, userId)
                ? repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal)
                : null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        log.info("delete {}", id);
        if (checkIfBelongsToAuthUser(repository.get(id), userId)) {
            repository.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, Integer userId) {
        log.info("get {}", id);
        Meal meal = repository.get(id);
        return checkIfBelongsToAuthUser(meal, userId) ? meal : null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        log.info("getAll");
        return repository.values().stream()
                .filter(m -> checkIfBelongsToAuthUser(m, userId))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private boolean checkIfBelongsToAuthUser(Meal meal, Integer userId) {
        return Objects.equals(meal.getUserId(), userId);
    }
}