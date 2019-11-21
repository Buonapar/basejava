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
            </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>