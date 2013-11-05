<%@tag description="User info page" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="user" type="ru.forxy.servlet.User" required="false" %>
<t:genericpage>
    <jsp:attribute name="header">
        <h1>Welcome, ${user != null ? user.name : "Guest"}</h1>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <p id="copyright">Copyright 2013, Forxy.ru</p>
    </jsp:attribute>

    <jsp:body>
        <jsp:doBody/>
    </jsp:body>
</t:genericpage>