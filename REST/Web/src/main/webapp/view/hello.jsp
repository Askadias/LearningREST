<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Hello <%= request.getAttribute("email")%></title></head>
<body>
Hello <%= request.getAttribute("email")%>
</body>
</html>