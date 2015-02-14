
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="lib_bundle.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <!-- Latest compiled and minified CSS -->
    <c:url value="/css/bootstrap.min.css" var="bootsrapMin" />
    <link rel="stylesheet" href="${bootsrapMin}"/>
    <%--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">--%>
    <!-- Optional theme -->
    <c:url value="/css/bootstrap-theme.min.css" var="bootsrapTheme" />
    <link rel="stylesheet" href="${bootsrapTheme}"/>
    <%--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">--%>
    <c:url value="/js/jquery-1.11.1.min.js" var="jquery1111" />
    <script src="${jquery1111}"> </script>
    <%--<script src="https://code.jquery.com/jquery-1.11.1.min.js" ></script>--%>
    <c:url value="/js/bootstrap.min.js" var="bootstrapJS" />
    <script src="${bootstrapJS}"> </script>
    <%--<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>--%>
    <title><tiles:getAsString name="title"/></title>
    <c:url value="/img/favicon.ico.png" var="favicon"/>
    <link rel="shortcut icon" href="${favicon}" type="image/jpg">

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='http://fonts.googleapis.com/css?family=Ubuntu+Condensed&subset=latin,cyrillic' rel='stylesheet' type='text/css'>
    <style type="text/css">
        body{
            font-family: 'Ubuntu Condensed', sans-serif;
            font-size: large;
        }
    </style>
</head>
<body >
<div class="container">
<tiles:insertAttribute name="header"/>
<table class="table" >
    <tr>
        <td class="col-md-2" style="vertical-align:top"><tiles:insertAttribute name="menu" /></td>
        <td class="col-md-9"><center><tiles:insertAttribute name="body"/></center></td>
    </tr>
</table>
<tiles:insertAttribute name="footer"/>
</div>
</body>
</html>
