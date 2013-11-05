<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="user" scope="session" class="ru.forxy.servlet.User"/>

<t:userpage user="${user}">
    <jsp:body>
        <c:choose>
            <c:when test="${!empty user.name}">

                <form action="${pageContext.request.contextPath}/logout.do">
                    <p>Hello, ${user.name}!</p>
                    <button type="submit">Logout</button>
                </form>

            </c:when>
            <c:otherwise>

                <form action="${pageContext.request.contextPath}/login.do">
                    <label title="Login:">
                        <input name="user" type="text" autofocus="true">
                    </label><br/>
                    <button type="submit">Login</button>
                </form>

            </c:otherwise>
        </c:choose>
    </jsp:body>
</t:userpage>