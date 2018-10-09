package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MapMealStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private Storage storage;

    @Override
    public void init() throws ServletException {
        storage = new MapMealStorage();
        MealsUtil.fillStorage(storage);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String action = request.getParameter("action");

        String idString = request.getParameter("id");
        int id = idString != null ? Integer.valueOf(idString) : 0;
        if (action == null) {
            request.setAttribute("list", MealsUtil.getFilteredWithExceeded(storage.getAll(),
                    LocalTime.MIN, LocalTime.MAX, 2000));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        Meal m;
        switch (action) {
            case "delete":
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "create":
                m = MealsUtil.EMPTY;
                break;
            case "edit":
                m = storage.getById(id);
                break;
            default:
                response.sendRedirect("meals");
                return;
        }

        request.setAttribute("meal", m);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int id = Integer.valueOf(request.getParameter("id"));
        LocalDateTime date = TimeUtil.parseToLocalDateTime(request.getParameter("datetime"));
        String description = request.getParameter("description");
        int calories = Integer.valueOf(request.getParameter("calories"));

        Meal m;
        if (id == 0) {
            m = new Meal(date, description, calories);
            storage.add(m);
        } else {
            m = new Meal(date, description, calories, id);
            storage.update(m);
        }
        response.sendRedirect("meals");
    }
}