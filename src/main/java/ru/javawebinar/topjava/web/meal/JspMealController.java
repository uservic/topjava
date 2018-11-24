package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("/meals")
    public String meals(@RequestParam(value = "action", required = false) String action,
                        @RequestParam(value = "id", required = false) String id,
                        Model model) {

        switch (action == null ? "all" : action) {
            case "delete":
                delete(getId(id));
                return "redirect:meals";
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        get(getId(id));
                model.addAttribute("meal", meal);
                return "/mealForm";
            case "all":
            default:
                model.addAttribute("meals", getAll());
                break;
        }
        return "meals";
    }

    @PostMapping("/meals")
    public String setMeal(@RequestParam(value = "action", required = false) String action,
                          @RequestParam(value = "dateTime", required = false) String dateTime,
                          @RequestParam(value = "description", required = false) String description,
                          @RequestParam(value = "calories", required = false) String calories,
                          @RequestParam(value = "id", required = false) String id,
                          @RequestParam(value = "startDate", required = false) String startDate,
                          @RequestParam(value = "endDate", required = false) String endDate,
                          @RequestParam(value = "startTime", required = false) String startTime,
                          @RequestParam(value = "endTime", required = false) String endTime,
                          Model model) {

        if (action == null) {
            Meal meal = new Meal(
                    LocalDateTime.parse(dateTime),
                    description,
                    Integer.parseInt(calories));

            if (id.isEmpty()) {
                create(meal);
            } else {
                update(meal, getId(id));
            }
            return "redirect:meals";

        } else if ("filter".equals(action)) {
            model.addAttribute("meals", getBetween(
                    parseLocalDate(startDate),
                    parseLocalTime(startTime),
                    parseLocalDate(endDate),
                    parseLocalTime(endTime))
            );
            return "meals";
        }
        return "redirect:index";
    }

    private int getId(String id) {
        String paramId = Objects.requireNonNull(id);
        return Integer.parseInt(paramId);
    }
}