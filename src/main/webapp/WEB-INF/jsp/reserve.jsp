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

<h1 align="center"><fmt:message key="RESERVATION_TITLE"/></h1>

<p><fmt:message key="RESERVE_MESSAGE"/></p>

<%--chosen car parameters start--%>

<table class="table table-striped table-bordered table-hover table-condensed">
  <tbody>
  <tr>
    <th><fmt:message key="CAR_ID" /></th>
    <th><fmt:message key="CAR_MODEL" /></th>
    <th><fmt:message key="CAR_BRAND" /></th>
    <th><fmt:message key="CAR_CLASS" /></th>
    <th><fmt:message key="PRICE" /></th>
    <th><fmt:message key="TRANSMISSION" /></th>
    <th><fmt:message key="DIESEL" /></th>
    <th><fmt:message key="AIR_CONDITION" /></th>
    <th><fmt:message key="DOOR_QTY" /></th>
  </tr>
  <tr>
    <td>${car.id}</td>
    <td>${car.model}</td>
    <td>${car.brand}</td>
    <td>${car.className}</td>
    <td>${car.price}</td>
    <td><fmt:message key="${car.isAutomat}" /></td>
    <td><fmt:message key="${car.isDiesel}" /></td>
    <td><fmt:message key="${car.hasCondition}" /></td>
    <td>${car.doorQty}</td>
  </tr>
  </tbody>
</table>
<%--chosen car parameters end--%>

<p><fmt:message key="RESERVE_APPROVAL" /></p>

<%--restrictions area start--%>
<c:if test="${empty user.userId}">
  <p class="bg-danger"><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> <fmt:message key="LOG_IN_WARN" /></p>
  <c:set var="reserveButtonClass" value="disabled"/>
</c:if>
<c:if test="${user.isAdmin == true}">
  <p class="bg-danger"><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> <fmt:message key="ADMIN_ACCESS_WARN" /></p>
  <c:set var="reserveButtonClass" value="disabled"/>
</c:if>

<c:if test="${empty car}">
  <p class="bg-danger"><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> <fmt:message key="NO_SESSION_CAR" /> </p>
  <c:set var="reserveButtonClass" value="disabled"/>
</c:if>

<c:if test="${not empty error}">
  <p class="bg-danger"><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> <fmt:message key="${error}" /> </p>
</c:if>
<%--restrictions area end--%>
<div class="row">
  <form role="form" action="" method="post">
    <div class="form-group col-md-4">
      <label for="beginDate" class="control-label"><fmt:message key="BEGIN_DATE" /></label>
      <div >
        <input class="form-control" id="beginDate" name="beginDate" type="date"  value="${carFilter.beginDate}" required />
      </div>
    </div>

    <div class="form-group col-md-4">
      <label for="endDate" class="control-label"><fmt:message key="END_DATE" /></label>
      <div >
        <input class="form-control" id="endDate" name="endDate" type="date" value="${carFilter.endDate}" required/>
      </div>
    </div>

    <div class="form-group col-md-4">
      <br/>
      <div>
        <button type="submit" class="btn btn-primary form-control ${reserveButtonClass}" name="command" value="reserve"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span> <fmt:message key="RESERVE" /></button>
      </div>
    </div>
  </form>
</div>

