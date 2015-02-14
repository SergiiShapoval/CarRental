<%@include file="../layout/lib_bundle.jsp"%>
<%@page contentType="text/html;charset=UTF-8" language="java"%>

<p class="text-success"> <fmt:message key="USER_INFO"/></p>

<table class="table  table-bordered table-hover">
    <tbody>
    <tr>
        <td><fmt:message key="USER_ID"/></td>
        <td>${userInfo.userId}</td>
    </tr>
    <tr>
        <td><fmt:message key="FIRST_NAME"/></td>
        <td>${userInfo.firstname}</td>
    </tr>
    <tr>
        <td><fmt:message key="LAST_NAME"/></td>
        <td>${userInfo.lastname}</td>
    </tr>
    <tr>
        <td><fmt:message key="PASSPORT"/></td>
        <td>${userInfo.passport}</td>
    </tr>
    <tr>
        <td><fmt:message key="EMAIL"/></td>
        <td>${userInfo.email}</td>
    </tr>

    </tbody>
</table>