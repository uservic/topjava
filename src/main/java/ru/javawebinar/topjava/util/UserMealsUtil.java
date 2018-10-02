package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
//        List<UserMealWithExceed> mealWithExceeds = _getFilteredWithExceeded(mealList, LocalTime.of(7, 0),
//                LocalTime.of(12, 0), 1500);

        List<UserMealWithExceed> mealWithExceeds = getFilteredWithExceeded(mealList, LocalTime.of(10, 0),
                LocalTime.of(14, 0), 1000);
        System.out.println(mealWithExceeds);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime,
                                                                   int caloriesPerDay) {
        return mealList.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate))
                .values()
                .stream()
                .filter(list -> list.stream()
                        .mapToInt(UserMeal::getCalories)
                        .sum() > caloriesPerDay)
                .flatMap(Collection::stream)
                .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(UserMealsUtil::convertToMealWithExceed)
                .collect(Collectors.toList());

    }

    public static List<UserMealWithExceed> _getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime,
                                                                    int caloriesPerDay) {
        List<UserMealWithExceed> result = new ArrayList<>();
        Map<LocalDate, Integer> calsPerDay = new HashMap<>();
        Map<LocalDate, List<UserMeal>> mealsPerDay = new HashMap<>();

        for (UserMeal meal : mealList) {
            LocalDate dayOfMonth = meal.getDate();
            calsPerDay.merge(dayOfMonth, meal.getCalories(), (a, b) -> a + b);

            List<UserMeal> userMeals = new ArrayList<>();
            userMeals.add(meal);
            mealsPerDay.merge(dayOfMonth, userMeals, (a, b) -> {
                List<UserMeal> list = new ArrayList<>(a);
                list.addAll(b);
                return list;
            });
        }

        for (Map.Entry<LocalDate, Integer> entry : calsPerDay.entrySet()) {
            if (entry.getValue() > caloriesPerDay) {
                mealsPerDay.get(entry.getKey())
                        .stream()
                        .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                        .map(UserMealsUtil::convertToMealWithExceed)
                        .forEach(result::add);
            }
        }
        return result;
    }

    private static UserMealWithExceed convertToMealWithExceed(UserMeal userMeal) {
        return new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                true);
    }
}