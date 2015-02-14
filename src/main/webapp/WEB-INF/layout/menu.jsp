<%@include file="lib_bundle.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="wrapper" align="center">
    <c:choose>
        <%--Salutation when logged in start--%>
        <c:when test="${not empty user && empty userError}">
            <p><fmt:message key="WELCOME"/> ${user.firstname}!</p>
            <div class="wrapper logout">
                <form name="logout" id="logout"  method="post" action="">
                    <strong  class="" onclick="document.getElementById('logout').submit()"><fmt:message key="LOG_OUT"/> </strong>
                    <input type="hidden" name="command" value="logout">
                </form>
            </div>
        </c:when>
        <%--Salutation when logged in end--%>
        <c:otherwise>
            <form action="" method="post" class="form-horizontal">
                    <%--Email field begin --%>
                <c:choose>
                    <c:when test="${not empty userError.email && not empty auth}">
                        <div class="alert alert-danger" role="alert">
                            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                            <span class="sr-only">Error:</span>
                            <fmt:message key="${userError.email}"/>
                        </div>
                        <c:set var="classEmail" value="has-error"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="classEmail" value=""/>
                    </c:otherwise>
                </c:choose>
                <div class="form-group ${classEmail}" style="margin-bottom: 0">
                    <label for="email" class=" control-label"><fmt:message key="EMAIL"/> <span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span> </label>
                    <div class="col-sm-12">
                        <input class="form-control" id="email" name="email" type="text" value="${user.email}" placeholder="email"/>
                    </div>
                </div>
                    <%--email field end --%>

                    <%--Password field begin --%>
                <c:choose>
                    <c:when test="${not empty userError.password && not empty auth}">
                        <div class="alert alert-danger" role="alert">
                            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                            <span class="sr-only">Error:</span>
                            <fmt:message key="${userError.password}"/>
                        </div>
                        <c:set var="classPassword" value="has-error"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="classPassword" value=""/>
                    </c:otherwise>
                </c:choose>
                <div class="form-group ${classPassword}" style="margin-bottom: 0">
                    <label for="password" class=" control-label"><fmt:message key="PASSWORD"/> <span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span> </label>
                    <div class="col-sm-12">
                        <input class="form-control" id="password" name="password" type="password" value="${user.password}" placeholder="password"/>
                    </div>
                </div>
                    <%--Password field end --%>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="cookieOn"> <fmt:message key="REMEMBER"/>
                    </label>
                </div >
                <button type="submit" class="btn btn-primary form-control" name="command" value="auth"><fmt:message key="SIGN_IN"/> </button>
            </form>
            <a href="/registration" class="small"><fmt:message key="REGISTRATION"/> </a> | <a href="/recover" class="small"><fmt:message key="FORGOT"/> </a>
        </c:otherwise>
    </c:choose>

    <%--links accessible for all start--%>
    <br/>
    <br/>
    <p><a href="/searchcar" class=""><fmt:message key="SEARCH_CARS"/></a></p>
    <c:choose>
        <c:when test="${empty userError && user.isAdmin == false}">
            <p><a href="/my_new_orders" class=""><fmt:message key="MY_NEW_ORDERS"/></a></p>
            <p><a href="/my_rejected_orders" class=""><fmt:message key="MY_REJECTED_ORDERS"/></a></p>
            <p><a href="/my_approved_orders" class=""><fmt:message key="MY_APPROVED_ORDERS"/></a></p>
            <p><a href="/my_closed_orders" class=""><fmt:message key="MY_CLOSED_ORDERS"/></a></p>
        </c:when>
         <c:when test="${empty userError && user.isAdmin == true}">
            <p><a href="/all_new_orders" class=""><fmt:message key="ALL_NEW_ORDERS"/></a></p>
            <p><a href="/all_approved_orders" class=""><fmt:message key="ALL_APPROVED_ORDERS"/></a></p>
            <p><a href="/all_rejected_orders" class=""><fmt:message key="ALL_REJECTED_ORDERS"/></a></p>
            <p><a href="/all_closed_orders" class=""><fmt:message key="ALL_CLOSED_ORDERS"/></a></p>
        </c:when>
    </c:choose>
    <%--links accessible for all end--%>
</div>