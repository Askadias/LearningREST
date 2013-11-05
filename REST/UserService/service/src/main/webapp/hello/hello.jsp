<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" scope="request" type="ru.forxy.servlet.User"/>

<t:userpage user="${user}">
</t:userpage>