<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <c:if test="${resume.uuid == null}">
        <title>Новое резюме</title>
    </c:if>
    <c:if test="${resume.uuid != null}">
        <title>Резюме ${resume.fullName}</title>
    </c:if>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <c:if test="${resume.uuid == null}">
            <input type="hidden" name="uuid" value="null">
        </c:if>
        <c:if test="${resume.uuid != null}">
            <input type="hidden" name="uuid" value="${resume.uuid}">
        </c:if>
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
                <dl>
                    <dt>${type.title}</dt>
                    <c:if test="${resume.uuid == null}">
                        <dd><textarea rows="10" name="${type.name()}" cols="100" ></textarea>
                    </c:if>
                    <c:if test="${resume.uuid != null}">
                        <dd><textarea rows="10" name="${type.name()}" cols="100" >${resume.sectionByType(type)}</textarea>
                    </c:if>
                    </dd>
                </dl>
            </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>