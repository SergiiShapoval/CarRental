
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="lib_bundle.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
    <script src="https://code.jquery.com/jquery-1.11.1.min.js" ></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <%--jquery cdn--%>
    <%--<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>--%>
    <title><tiles:getAsString name="title"/></title>
    <link rel="shortcut icon" href="https://lh6.googleusercontent.com/-hT32iGH_vO4/VK4DWz6AuUI/AAAAAAAAHbY/EMr2BDjydqs/s16/favicon.jpg" type="image/jpg">
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
