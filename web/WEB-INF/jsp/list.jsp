<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="java.util.List" %><%--
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
    <section>
        <table border="1" cellpadding="0" align="center">
            <caption>Hello Resumes</caption>
            <tr>
                <th>Имя</th>
                <th>Email</th>
            </tr>
            <%
                for (Resume resume : (List<Resume>)request.getAttribute("resumes")) {
            %>
            <tr>
                <td><a href="resume?uuid=<%=resume.getUuid()%>"><%=resume.getFullName()%></a></td>
                <td><%=resume.getContacts(ContactType.EMAIL)%></td>
            </tr>
            <%
                }
            %>
        </table>
    </section>
</body>
</html>
