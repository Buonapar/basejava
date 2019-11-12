<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Алексей
  Date: 12.11.2019
  Time: 16:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../../css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
    <section>
        <table border="1" cellpadding="0" align="center">
            <caption>Hello Resumes</caption>
            <tr>
                <th>Имя</th>
                <th>Email</th>
            </tr>
            <c:forEach items="${resumes}" var="resume">
                <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume"/>
                <tr>
                    <td><a href="resume?uuid=${resume.uuid}">${resume.fullName}</a></td>
                    <td><%=resume.getContacts(ContactType.EMAIL)%></td>
                </tr>
            </c:forEach>
        </table>
    </section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
