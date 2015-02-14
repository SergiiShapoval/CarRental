<%@include file="../layout/lib_bundle.jsp"%>
<%--
  Created by IntelliJ IDEA.
  User: Сергей
  Date: 27.12.2014
  Time: 6:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="language"/>

<h1 align="center"><fmt:message key="${message}"/></h1>

<%--My order data start--%>
<table class="table table-striped table-bordered table-hover table-condensed">
  <tbody>
  <tr>
    <th><fmt:message key="ORDER_ID" /></th>
    <th><fmt:message key="CAR_ID" /></th>
    <th><fmt:message key="CAR_MODEL" /></th>
    <th><fmt:message key="CAR_BRAND" /></th>
    <th><fmt:message key="BEGIN_DATE" /></th>
    <th><fmt:message key="END_DATE" /></th>
    <th><fmt:message key="TOTAL_RENT" /></th>
    <th><fmt:message key="REASON" /></th>
    <th><fmt:message key="PENALTY" /></th>
  </tr>
  <c:forEach items="${orders}" var="order">
    <tr>
      <td><fmt:formatNumber value="${order.orderId}" /> </td>
      <td><fmt:formatNumber value="${order.carId}"/> </td>
      <td>${order.model}</td>
      <td>${order.brand}</td>
      <td><fmt:formatDate value="${order.dateStart}" type="date"/></td>
      <td><fmt:formatDate value="${order.dateEnd}" type="date"/></td>
      <td><fmt:formatNumber value="${order.rentTotal}"/></td>
      <td>${order.reason}</td>
      <td><fmt:formatNumber value="${order.penalty}"/></td>
    </tr>
  </c:forEach>
  </tbody>
</table>

<%--My order data start--%>

