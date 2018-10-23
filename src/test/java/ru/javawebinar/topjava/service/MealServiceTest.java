package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(100008, ADMIN_ID);
        assertMatch(meal, ADMIN_Meal_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(1, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(100008, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_Meal_2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(1, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> list = service.getBetweenDates(LocalDate.of(2015, 5, 31),
                LocalDate.of(3000, 1, 1), USER_ID);
        assertMatch(list, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> list = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 13, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 13, 0), USER_ID);
        assertMatch(list, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void update() {
        Meal updated = new Meal(ADMIN_Meal_2);
        updated.setDescription("Плотный ужин");
        updated.setCalories(3500);
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(100009, ADMIN_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(MOCK_MEAL, USER_ID);
    }

    @Test
    public void create() {
        Meal mealForSave = MOCK_NEW_MEAL;
        Meal created = service.create(mealForSave, ADMIN_ID);
        mealForSave.setId(created.getId());
        assertMatch(service.getAll(ADMIN_ID), ADMIN_Meal_2, ADMIN_Meal_1, mealForSave);
    }
}