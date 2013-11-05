<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="user" scope="request" type="ru.forxy.servlet.User"/>

<t:userpage user="${user}">
    <jsp:body>
        <c:if test="${!empty user}">

            <form action="${pageContext.request.contextPath}/logout.do">
                <p>Hello, ${user.name}!</p>
                <button type="submit">Logout</button>
            </form>

        </c:if>
        <c:else>

            <form action="${pageContext.request.contextPath}/login.do">
                <label title="Login:">
                    <input name="user" type="text" autofocus="true">
                </label><br/>
                <button type="submit">Login</button>
            </form>

        </c:else>
    </jsp:body>
</t:userpage>