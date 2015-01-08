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

<h1 align="center"><fmt:message key="ORDER_TITLE"/></h1>

<form role="form" action="" method="post">
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
      <th><fmt:message key="STATUS" /></th>
    </tr>
    <tr>
      <td><fmt:formatNumber value="${order.orderId}" /> </td>
      <td><fmt:formatNumber value="${order.carId}"/> </td>
      <td>${order.model}</td>
      <td>${order.brand}</td>
      <td><fmt:formatDate value="${order.dateStart}" type="date"/></td>
      <td><fmt:formatDate value="${order.dateEnd}" type="date"/></td>
      <td><fmt:formatNumber value="${order.rentTotal}"/></td>
      <%--Script to disable reject button, when reason not provided start--%>
      <script type="text/javascript">
        function onReasonChange(){
          if (document.getElementById('reason').value != ""){
            document.getElementById('REJECT_ORDER').className = "btn btn-primary form-control";
          } else {
            document.getElementById('REJECT_ORDER').className =   "btn btn-primary form-control disabled";
          }
        }
      </script>
      <%--Script to disable reject button, when reason not provided end--%>
      <td><input class="form-control" id="reason" name="reason" type="text" value="${order.reason}" onchange="onReasonChange()"/></td>
      <%--Script to disable add penalty button, when amount not provided start--%>
      <script type="text/javascript">
        function onPenaltyChange(){
          if (document.getElementById('penalty').value >= 0.01){
            document.getElementById('ADD_PENALTY').className = "btn btn-primary form-control";
          } else {
            document.getElementById('ADD_PENALTY').className =   "btn btn-primary form-control disabled";
          }
        }
      </script>
      <%--Script to disable add penalty button, when amount not provided end--%>
      <td><input class="form-control" id="penalty" name="penalty" type="number" step="0.01" min="0" value="${order.penalty}" onchange="onPenaltyChange()"/></td>
      <td>${order.status}</td>
    </tr>
    </tbody>
  </table>
  <c:if test="${not empty error}">
    <p class="bg-danger"><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> <fmt:message key="${error}" /> </p>
  </c:if>
  <div class="form-group col-md-3">
    <%--disable approve button, when status not null start--%>
    <c:choose >
      <c:when test="${order.status == 'new'}">
        <c:set var="APPROVE_ORDER_class" value=""/>
      </c:when>
      <c:otherwise>
        <c:set var="APPROVE_ORDER_class" value="disabled"/>
      </c:otherwise>
    </c:choose>
    <%--disable approve button, when status not null start--%>
    <button type="submit" class="btn btn-primary form-control ${APPROVE_ORDER_class}" id="APPROVE_ORDER" name="command" value="approve_order"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span> <fmt:message key="APPROVE_ORDER"/></button>
  </div>
  <div class="form-group col-md-3">
    <%--button disabled until any change in reason--%>
    <button type="submit" class="btn btn-primary form-control disabled" id="REJECT_ORDER" name="command" value="reject_order"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> <fmt:message key="REJECT_ORDER"/></button>
  </div>
  <div class="form-group col-md-3">
    <%--button disabled until any change in penalty--%>
    <button type="submit" class="btn btn-primary form-control  disabled" id="ADD_PENALTY" name="command" value="add_penalty"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> <fmt:message key="ADD_PENALTY"/></button>
  </div>
  <div class="form-group col-md-3">
<%--button disabled if status != approved start--%>    
    <c:choose >
      <c:when test="${order.status == 'approved'}">
        <c:set var="CLOSE_ORDER_class" value=""/>
      </c:when>
      <c:otherwise>
        <c:set var="CLOSE_ORDER_class" value="disabled"/>
      </c:otherwise>
    </c:choose>
<%--button disabled if status != approved start--%>
    <button type="submit" class="btn btn-primary form-control ${CLOSE_ORDER_class}" id="CLOSE_ORDER" name="command" value="close_order"><span class="glyphicon glyphicon-ok-sign" aria-hidden="true"></span> <fmt:message key="CLOSE_ORDER"/></button>
  </div>
</form>


