<%@include file="../layout/lib_bundle.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h1 align="center"><fmt:message key="RECOVER_PAGE_TITLE"/> </h1>
<p align="center"><fmt:message key="RECOVER_PAGE_MSG"/> </p>

<form action="/recover" method="post" class="form-horizontal">
<%--Email field begin --%>
<%--error handling for email start--%>
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
<%--error handling for email end--%>
    <div class="form-group ${classEmail}">
        <label for="email" class="col-md-2 control-label"><fmt:message key="EMAIL"/> <span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span> </label>
        <div class="col-md-10">
            <input class="form-control" id="email" name="email" type="text" value="${user.email}" placeholder="email"/>
        </div>
    </div>
<%--email field end --%>
    <br/>
    <div class="form-group">
        <div class="col-md-offset-2 col-md-10">
            <button type="submit" class="btn btn-primary form-control" name="command" value="recover"><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> <fmt:message key="RECOVER"/></button>
        </div>
    </div>
</form>

