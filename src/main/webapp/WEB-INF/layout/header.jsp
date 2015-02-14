<%@include file="lib_bundle.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<div style="background-image: linear-gradient( to bottom right, #F8961C 0%, #FFD007 100%);">
    <%--submit actions of language spans--%>
    <script type="text/javascript">
        function changelang(lang_id){
            document.getElementById('lang_id').value = lang_id;
            if (lang_id == "RU"){
                document.getElementById('lang_en').className = "text-muted small";
                document.getElementById('lang_ru').className = "text-primary";
            } else {
                document.getElementById('lang_ru').className =   "text-muted small";
                document.getElementById('lang_en').className =   "text-primary";
            }
            document.getElementById('lang_change').submit();
        }
    </script>
    <%--submit actions of language spans--%>
    <%--change language bar colors start--%>
    <c:choose>
        <c:when test="${not empty lang_id && lang_id=='EN'}">
            <c:set var="classEN" value="text-primary "/>
            <c:set var="classRU" value="text-muted small"/>
        </c:when>
        <c:when test="${not empty lang_id && lang_id=='RU'}">
            <c:set var="classRU" value="text-primary "/>
            <c:set var="classEN" value="text-muted small"/>
        </c:when>
    </c:choose>
    <%--change language bar colors end--%>
    <form style="float: right"  name="lang_change" id="lang_change"  method="post" action="">
        <strong  class="${classEN}" id="lang_en" onclick="changelang('EN')">EN</strong> | <strong  class="${classRU}" id="lang_ru" onclick="changelang('RU')">RU</strong>
        <input type="hidden" name="lang_id" id="lang_id" value="">
        <input type="hidden" name="command" value="language">
    </form>

    <table>
        <tr>
            <td class="col-md-3">
                <c:url value="/img/family.rental.cars.png" var="headerImg"/>
                <img src="${headerImg}" style="max-width:320px;"/>
                <%--<img src="http://www.familyrentalcars.com/images/bodrum.family.rental.cars.png" style="max-width:320px;" />--%>
            </td>
            <td class="col-md-9" style="vertical-align: middle; display: table-cell; text-align: right" >
                <h1 style="color: #1C75BC; font-weight: bold">   <fmt:message key="HEADER_TITLE"/> </h1>
            </td>
        </tr>
    </table>

</div>