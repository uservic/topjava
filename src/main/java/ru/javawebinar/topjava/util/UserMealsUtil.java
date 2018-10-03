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
        List<UserMealWithExceed> mealWithExceeds = _getFilteredWithExceeded(mealList, LocalTime.of(7, 0),
                LocalTime.of(14, 0), 2000);

//        List<UserMealWithExceed> mealWithExceeds = getFilteredWithExceeded(mealList, LocalTime.of(7, 0),
//                LocalTime.of(11, 0), 2000);
        System.out.println(mealWithExceeds);
    }

//    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime,
//                                                                   int caloriesPerDay) {
//        /* Предполагаемый алгоритм стрима:
//        *   - собрать мапу из списка UserMeal с ключом date (LocalDate immutable) и значенями-списками соотв UserMeals
//        *   - пройтись по каждому объекту UserMeal из списков и, если сумма калорий для списка > caloriesPerDay,
//        *       собрать новый объект UserMealWithExceed с параметром exceed = true, если <, то exceed = false
//        *   - из полученных объектов собрать результирующий список */
//
//        return mealList.stream()
//                .collect(Collectors.groupingBy(UserMeal::getDate))
//                .values()
//                .stream()
//
//                (list -> list.stream()
//                        .map(m-> list.stream()
//                            .mapToInt(UserMeal::getCalories)
//                            .sum() > caloriesPerDay
//                        ? convertToMealWithExceed(m, true)
//                        : convertToMealWithExceed(m, false))
//                        . filter(me -> TimeUtil.isBetween(me.getDateTime().toLocalTime(), startTime, endTime))
//                        .collect(Collectors.toList()));
//
//    }

    public static List<UserMealWithExceed> _getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime,
                                                                    int caloriesPerDay) {
        Map<LocalDate, Integer> calsPerDay = new HashMap<>();
        Map<LocalDate, List<UserMeal>> mealsPerDay = new HashMap<>();
        for (UserMeal meal : mealList) {
            LocalDate dayOfMonth = meal.getDate();
            calsPerDay.merge(dayOfMonth, meal.getCalories(), Integer::sum);

            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                List<UserMeal> userMeals = new ArrayList<>();
                userMeals.add(meal);
                mealsPerDay.merge(dayOfMonth, userMeals, (a, b) -> {
                    List<UserMeal> list = new ArrayList<>(a);
                    list.addAll(b);
                    return list;
                });
            }
        }

        List<UserMealWithExceed> result = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : calsPerDay.entrySet()) {
            List<UserMeal> userMealList = mealsPerDay.get(entry.getKey());
            if (entry.getValue() > caloriesPerDay) {
                result.addAll(convertToMealWithExceedList(userMealList, true));
            } else {
                result.addAll(convertToMealWithExceedList(userMealList, false));
            }
        }
        return result;
    }

    private static List<UserMealWithExceed> convertToMealWithExceedList(List<UserMeal> list, boolean exceed) {
        return list.stream()
                .map(me -> new UserMealWithExceed(me.getDateTime(), me.getDescription(), me.getCalories(),
                        exceed))
                .collect(Collectors.toList());

    }

    private static UserMealWithExceed convertToMealWithExceed(UserMeal userMeal, boolean exceed) {
        return new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                exceed);
    }
}