<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit">Edit</a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contact}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.Section>"/>
        <h3><%=sectionEntry.getKey().getTitle()%><a> : </a></h3>
            <jsp:useBean id="textSection" type="ru.javawebinar.basejava.model.TextSection" scope="request"/>
            <jsp:useBean id="textListSection" type="ru.javawebinar.basejava.model.TextListSection" scope="request"/>
            <c:set var="sectionType" value="${sectionEntry.key.title}"/>
            <c:choose>
                <c:when test="${sectionType == 'Личные качества' ||
                                sectionType == 'Позиция'}">${textSection=sectionEntry.value;
                                                             textSection.get()}
                </c:when>
                <c:when test="${sectionType == 'Достижения' ||
                                sectionType == 'Квалификация'}">${textListSection=sectionEntry.value;
                                                                  textListSection.toStringHtml()}
                </c:when>
            </c:choose>
        </c:forEach>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
