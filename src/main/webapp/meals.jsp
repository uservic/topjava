<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<section>
    <a href="meals?id=0&action=create">Добавить прием пищи</a>
    <br>
    <br>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Название</th>
            <th>Количество калорий</th>
            <th>Дата</th>
        </tr>
        <c:forEach var="meal" items="${list}">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealWithExceed"/>
            <tr class="${meal.exceed ?'red':'green'}">
                <td >${meal.description}</td>
                <td style="text-align: center">${meal.calories}</td>
                <td>${TimeUtil.parseToString(meal.dateTime)}</td>
                <td><a href="meals?id=${meal.id}&action=edit">Редактировать</a></td>
                <td><a href="meals?id=${meal.id}&action=delete">Удалить</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>