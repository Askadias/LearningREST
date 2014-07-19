<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Hello <%= request.getAttribute("tokenKey")%>
</title></head>
<body>
Hello <%= request.getAttribute("tokenKey")%>
</body>
</html>