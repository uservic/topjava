package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final Meal USER_MEAL_1 = new Meal(100002, LocalDateTime.of(2015, Month.MAY, 30, 10, 0),
            "Завтрак", 500);
    public static final Meal USER_MEAL_2 = new Meal(100003, LocalDateTime.of(2015, Month.MAY, 30, 13, 0),
            "Обед", 1000);
    public static final Meal USER_MEAL_3 = new Meal(100004, LocalDateTime.of(2015, Month.MAY, 30, 20, 0),
            "Ужин", 500);
    public static final Meal USER_MEAL_4 = new Meal(100005, LocalDateTime.of(2015, Month.MAY, 31, 10, 0),
            "Завтрак", 1000);
    public static final Meal USER_MEAL_5 = new Meal(100006, LocalDateTime.of(2015, Month.MAY, 31, 13, 0),
            "Обед", 500);
    public static final Meal USER_MEAL_6 = new Meal(100007, LocalDateTime.of(2015, Month.MAY, 31, 20, 0),
            "Ужин", 510);
    public static final Meal ADMIN_Meal_1 = new Meal(100008, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0),
            "Админ ланч", 510);
    public static final Meal ADMIN_Meal_2 = new Meal(100009, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0),
            "Админ ужин", 1500);
    public static final Meal MOCK_MEAL = new Meal(1, LocalDateTime.of(1, Month.JANUARY, 1, 1, 1),
            "Mock", 1);
    public static final Meal MOCK_NEW_MEAL = new Meal(LocalDateTime.of(1, Month.JANUARY, 1, 1, 1),
            "Mock New", 1);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
