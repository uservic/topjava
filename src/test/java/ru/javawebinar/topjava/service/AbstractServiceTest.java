package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
//@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class AbstractServiceTest {
    private static final Logger log = getLogger("result");
    private static StringBuilder results = new StringBuilder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    static {
        // needed only for java.util.logging (postgres driver)
        SLF4JBridgeHandler.install();
    }

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    @Autowired
    private UserService userService;

    @Autowired
    private MealService mealService;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() {
        cacheManager.getCache("users").clear();
    }

    @Test
    public void create() {
        User newUser = UserTestData.getCreated();
        User createdUser = userService.create(newUser);
        newUser.setId(createdUser.getId());
        assertMatch(userService.getAll(), ADMIN, newUser, USER);

        Meal createdMeal = MealTestData.getCreated();
        mealService.create(createdMeal, USER_ID);
        assertMatch(mealService.getAll(USER_ID), createdMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void delete() {
        userService.delete(USER_ID);
        assertMatch(userService.getAll(), ADMIN);

        mealService.delete(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(mealService.getAll(ADMIN_ID), ADMIN_MEAL2);
    }

    @Test
    public void deletedNotFound() {
        thrown.expect(NotFoundException.class);
        userService.delete(1);

        thrown.expect(NotFoundException.class);
        mealService.delete(MEAL1_ID, 1);
    }

    @Test
    public void get() {
        User user = userService.get(USER_ID);
        assertMatch(user, USER);

        Meal meal = mealService.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(meal, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() {
        thrown.expect(NotFoundException.class);
        userService.get(1);

        thrown.expect(NotFoundException.class);
        mealService.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() {
        User updatedUser = new User(USER);
        updatedUser.setName("UpdatedName");
        updatedUser.setCaloriesPerDay(330);
        userService.update(updatedUser);
        assertMatch(userService.get(USER_ID), updatedUser);

        Meal updatedMeal = getUpdated();
        mealService.update(updatedMeal, USER_ID);
        assertMatch(mealService.get(MEAL1_ID, USER_ID), updatedMeal);
    }

    @Test
    public void getAll() {
        assertMatch(userService.getAll(), ADMIN, USER);

        assertMatch(mealService.getAll(USER_ID), MEALS);
    }

    @Test
    public void getByEmail() throws Exception {
        User user = userService.getByEmail("user@yandex.ru");
        assertMatch(user, USER);
    }

    @Test
    public void duplicateMailCreate() {
        thrown.expect(DataAccessException.class);
        userService.create(getCreatedWithDuplicateMail());
    }

    @Test
    public void updateNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + MEAL1_ID);
        mealService.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(mealService.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30),
                USER_ID), MEAL3, MEAL2, MEAL1);
    }
}
