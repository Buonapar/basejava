<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>${param.uuid == null ? 'Новое резюме' : resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
            <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
            <c:forEach var="type" items="<%=ContactType.values()%>">
               <dl>
                    <dt>${type.title}</dt>
                    <dd><input type="text" name="${type.name()}" size="30" value="${resume.getContacts(type)}"></dd>
               </dl>
            </c:forEach>
            <c:forEach var="type" items="<%=SectionType.values()%>">
                    <c:if test="${type.name() != 'EXPERIENCE' && type.name() != 'EDUCATION'}">
                        <dl>
                        <dt>${type.title}</dt>
                        <dd><textarea rows="10" name="${type.name()}" cols="100" >${resume.uuid == null ?
                                                                                    "" : resume.sectionByType(type)}</textarea>
                        </dd>
                        </dl>
                    </c:if>
                <c:if test="${type.name() == 'EXPERIENCE' || type.name() == 'EDUCATION'}">
                    <h3>${type.title}:</h3>
                    <c:forEach var="companies" items="${resume.CompanySection(type).get()}">
                        <dl>
                            <dt style="margin-left: 10px">Название компании</dt>
                            <dd><input type="text" name="${type.name()}" size="30" value="${companies.homepage.getName()}"></dd>
                        </dl>
                        <dl>
                            <dt style="margin-left: 10px">Сайт компании</dt>
                            <dd><input type="text" name="${type.name()}url" size="30" value="${companies.homepage.getUrl()}"></dd>
                        </dl>
                        <c:forEach var="positions" items="${companies.getPositions()}">
                            <input type="hidden" name="${type.name()}numberPosition" value="${companies.getPositions().size()}" >
                            <dt style="margin-left: 20px">Позиции</dt>
                            <dl>
                                <dt style="margin-left: 30px">Должность</dt>
                                <dd><input type="text" name="${type.name()}title" size="30" value="${positions.getTitle()}"></dd>
                            </dl>
                            <dl>
                                <dt style="margin-left: 30px">Дата начала работы</dt>
                                <dd><input type="month" name="${type.name()}startDate" size="30" value="${positions.getStartDate()}"></dd>
                            </dl>
                            <dl>
                                <dt style="margin-left: 30px">Дата окончания работы</dt>
                                <dd><input type="month" name="${type.name()}endDate" size="30" value="${positions.getEndDate()}"></dd>
                            </dl>
                            <dl>
                                <dt style="margin-left: 30px">Обязанности</dt>
                                <dd><textarea rows="3" name="${type.name()}description" cols="70">${positions.getDescription()}</textarea></dd>
                            </dl>
                        </c:forEach>
                    </c:forEach>
                </c:if>
            </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>