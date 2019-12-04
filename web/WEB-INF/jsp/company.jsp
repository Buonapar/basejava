<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <jsp:useBean id="section" type="ru.javawebinar.basejava.model.SectionType" scope="request"/>
    <title>Title</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<form method="post" action="resume" enctype="application/x-www-form-urlencoded">
<h3>${section.title}:</h3>
    <input type="hidden" name="uuid" value="${resume.uuid}">
    <input type="hidden" name="section" value="${section}">
    <dl>
    <dt style="margin-left: 10px">Название компании</dt>
    <dd><input type="text" name="name" size="30"></dd>
</dl>
    <dl>
    <dt style="margin-left: 10px">Сайт компании</dt>
    <dd><input type="text" name="url" size="30"></dd>
</dl>
    <dt style="margin-left: 20px">Позиции</dt>
    <dl>
    <dt style="margin-left: 30px">Должность</dt>
    <dd><input type="text" name="title" size="30"></dd>
    </dl>
    <dl>
    <dt style="margin-left: 30px">Дата начала работы</dt>
    <dd><input type="month" name="startDate" size="30"></dd>
    </dl>
    <dl>
    <dt style="margin-left: 30px">Дата окончания работы</dt>
    <dd><input type="month" name="endDate" size="30"></dd>
    </dl>
    <dl>
    <dt style="margin-left: 30px">Обязанности</dt>
    <dd><textarea rows="3" name="description" cols="70"></textarea></dd>
    </dl>
    <hr>
    <button type="submit">Сохранить</button>
    <button onclick="window.history.back()">Отменить</button>
</form>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
