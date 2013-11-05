<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
    <jsp:body>
        <jsp:useBean id="exception" scope="request" class="java.lang.Throwable"/>
        <p>
                ${exception.message} <br/>
            Details:<br/>
                ${exception.stackTrace}
        </p>
    </jsp:body>

    <jsp:attribute name="footer">
        <p id="copyright">Copyright 2013, Forxy.ru</p>
    </jsp:attribute>
</t:genericpage>