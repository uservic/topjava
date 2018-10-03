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
//                LocalTime.of(14, 0), 2000);

        List<UserMealWithExceed> mealWithExceeds = getFilteredWithExceeded(mealList, LocalTime.of(7, 0),
                LocalTime.of(14, 0), 2000);
        System.out.println(mealWithExceeds);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime,
                                                                   int caloriesPerDay) {
        Map<LocalDate, Integer> calsPerDay = mealList.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream()
                .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> convertToMealWithExceed(m, calsPerDay.get(m.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> _getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime,
                                                                    int caloriesPerDay) {
        Map<LocalDate, Integer> calsPerDay = new HashMap<>();
        for (UserMeal meal : mealList) {
            calsPerDay.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExceed> resultList = new ArrayList<>();
        for (UserMeal m : mealList) {
            if (TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime)) {
                resultList.add(convertToMealWithExceed(m, (calsPerDay.get(m.getDate()) > caloriesPerDay)));
            }
        }
        return resultList;
    }

    private static UserMealWithExceed convertToMealWithExceed(UserMeal userMeal, boolean exceed) {
        return new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                exceed);
    }
}