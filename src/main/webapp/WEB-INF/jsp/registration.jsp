<%@include file="../layout/lib_bundle.jsp"%>
<%--
  Created by IntelliJ IDEA.
  User: Сергей
  Date: 27.12.2014
  Time: 6:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1 align="center"><fmt:message key="REGISTRATION_PAGE_TITLE"/></h1>
<form action="/registration" method="post" class="form-horizontal">
  <%--First name field begin --%>
  <%--error handling start--%>
  <c:choose>
    <c:when test="${not empty userError.firstname}">
      <div class="alert alert-danger" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <span class="sr-only"><fmt:message key="ERROR"/>:</span>
        <fmt:message key="${userError.firstname}"/>
      </div>
      <c:set var="classFirstName" value="has-error"/>
    </c:when>
    <c:otherwise>
      <c:set var="classFirstName" value=""/>
    </c:otherwise>
  </c:choose>
  <%--error handling end--%>
  <div class="form-group ${classFirstName}">
    <label for="firstname" class="col-md-2 control-label"><fmt:message key="FIRST_NAME"/><span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span> </label>
    <div class="col-md-10">
      <input class="form-control" id="firstname" name="firstname" type="text" value="${user.firstname}" placeholder="firstname"/>
    </div>
  </div>
  <%--First name field end --%>
  <%--Last name field begin --%>
    <%--error handling start--%>
  <c:choose>
    <c:when test="${not empty userError.lastname}">
      <div class="alert alert-danger" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <span class="sr-only"><fmt:message key="ERROR"/>:</span>
        <fmt:message key="${userError.lastname}"/>
      </div>
      <c:set var="classLastName" value="has-error"/>
    </c:when>
    <c:otherwise>
      <c:set var="classLastName" value=""/>
    </c:otherwise>
  </c:choose>
    <%--error handling end--%>
  <div class="form-group ${classLastName}">
    <label for="lastname" class="col-md-2 control-label"><fmt:message key="LAST_NAME"/><span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span> </label>
    <div class="col-md-10">
      <input class="form-control" id="lastname" name="lastname" type="text" value="${user.lastname}" placeholder="lastname"/>
    </div>
  </div>
  <%--Last name field end --%>

  <%--Email field begin --%>
    <%--error handling start--%>
  <c:choose>
    <c:when test="${not empty userError.email}">
      <div class="alert alert-danger" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <span class="sr-only"><fmt:message key="ERROR"/>:</span>
        <fmt:message key="${userError.email}"/>
      </div>
      <c:set var="classEmail" value="has-error"/>
    </c:when>
    <c:otherwise>
      <c:set var="classEmail" value=""/>
    </c:otherwise>
  </c:choose>
    <%--error handling end--%>
  <div class="form-group ${classEmail}">
    <label for="email" class="col-md-2 control-label"><fmt:message key="EMAIL"/><span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span> </label>
    <div class="col-md-10">
      <input class="form-control" id="email" name="email" type="text" value="${user.email}" placeholder="email"/>
    </div>
  </div>
  <%--email field end --%>
  <%--Passport field begin --%>
    <%--error handling start--%>
  <c:choose>
    <c:when test="${not empty userError.passport}">
      <div class="alert alert-danger" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <span class="sr-only"><fmt:message key="ERROR"/>:</span>
        <fmt:message key="${userError.passport}"/>
      </div>
      <c:set var="classPassport" value="has-error"/>
    </c:when>
    <c:otherwise>
      <c:set var="classPassport" value=""/>
    </c:otherwise>
  </c:choose>
    <%--error handling end--%>
  <div class="form-group ${classPassport}">
    <label for="passport" class="col-md-2 control-label"><fmt:message key="PASSPORT"/><span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span> </label>
    <div class="col-md-10">
      <input class="form-control" id="passport" name="passport" type="text" value="${user.passport}" placeholder="passport"/>
    </div>
  </div>
  <%--Passport field end --%>
  <%--Password field begin --%>
    <%--error handling start--%>
  <c:choose>
    <c:when test="${not empty userError.password}">
      <div class="alert alert-danger" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <span class="sr-only"><fmt:message key="ERROR"/>:</span>
        <fmt:message key="${userError.password}"/>
      </div>
      <c:set var="classPassword" value="has-error"/>
    </c:when>
    <c:otherwise>
      <c:set var="classPasswordt" value=""/>
    </c:otherwise>
  </c:choose>
    <%--error handling end--%>
  <div class="form-group ${classPassword}">
    <label for="password" class="col-md-2 control-label"><fmt:message key="PASSWORD"/> <span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span> </label>
    <div class="col-md-10">
      <input class="form-control" id="password" name="password" type="password" value="${user.password}" placeholder="password"/>
    </div>
  </div>
  <%--Password field end --%>

  <div class="form-group">
    <label for="passwordCheck" class="col-md-2 control-label"><fmt:message key="REPEAT"/><span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span></label>
    <div class="col-md-10">
      <input class="form-control" id="passwordCheck" name="passwordCheck" type="password"  value="${user.passwordCheck}" placeholder="repeat password"/>
    </div>
  </div>

  <div class="form-group">
    <div class="col-md-offset-2 col-md-10">
      <button type="submit" class="btn btn-primary form-control" name="command" value="register"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span> <fmt:message key="REGISTER"/></button>
    </div>
  </div>
</form>


