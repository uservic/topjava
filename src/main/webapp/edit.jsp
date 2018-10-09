<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <input type="hidden" name="id" value="${meal.id}">

    <dl>
        <dt><h4>Описание пищи</h4></dt>
        <dd><input type="text" name="description" size="35" value="${meal.description}"></dd>
        <dt><h4>Количество калорий</h4></dt>
        <dd><input type="text" name="calories" size="10" value="${meal.calories}"></dd>
        <dt><h4>Дата</h4></dt>
        <dd><input type="text" name="datetime" size="35" required="required" placeholder="ГГГГ-ММ-ДД чч:мм" value="${TimeUtil.parseToString(meal.dateTime)}"></dd>
    </dl>
    <button type="submit">Сохранить</button>
    <button onclick="window.history.back()">Назад</button>
</form>
</body>
</html>