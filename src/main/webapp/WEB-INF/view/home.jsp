<%--
  Created by IntelliJ IDEA.
  User: hongducphan
  Date: 12/20/19
  Time: 21:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home Page</title>
    <style>
        .error {
            color: red;
        }
    </style>
</head>
<body>
    <h1>Home Page</h1>
    <span class="error">${error}</span>
    <table border="1">
        <tr>
            <th>No</th>
            <th>First name</th>
            <th>Last name</th>
            <th>Email</th>
            <th>Action</th>
        </tr>
        <c:forEach var="customer" items="${customers}" varStatus="loop">
            <c:url var="deleteLink" value="/customer/delete">
                <c:param name="customerId" value="${customer.id}"/>
            </c:url>
            <tr>
                <td>${loop.index + 1}</td>
                <td>${customer.firstName}</td>
                <td>${customer.lastName}</td>
                <td>${customer.email}</td>
                <td>
                    <a href="${deleteLink}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
