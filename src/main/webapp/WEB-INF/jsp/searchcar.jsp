<%@include file="../layout/lib_bundle.jsp"%>
<%@page contentType="text/html;charset=UTF-8" language="java"%>

<fmt:setBundle basename="language" var="lang"/>
<fmt:setBundle basename="sqlstatements" var="sql"/>

<%--search panel start--%>
<%--panel remember filter criteria and language sensitive, price verification through html5--%>
<form role="form" action="/searchcar" method="post" class="wrapper">
    <div class="row">
        <div class="form-group col-md-3">
            <label for="className" class="control-label"><fmt:message key="CLASS_OF_CAR" bundle="${lang}"/> </label>
            <select class="form-control" name="className" id="className">
                <option ${carFilter.className=="A" ? 'selected' : ''}>A</option>
                <option ${carFilter.className=="B" ? 'selected' : ''}>B</option>
                <option ${carFilter.className=="C" ? 'selected' : ''}>C</option>
                <option ${carFilter.className=="D" ? 'selected' : ''}>D</option>
                <fmt:message key="ANY" bundle="${lang}" var="ANY"/>
                <option ${carFilter.className==ANY ? 'selected' : ''}>${ANY}</option>
            </select>
        </div>
        <div class="form-group col-md-3">
            <label for="isAutomat" class="control-label"><fmt:message key="TRANSMISSION" bundle="${lang}"/></label>
            <select class="form-control" name="isAutomat" id="isAutomat">
                <fmt:message key="AUTOMAT" bundle="${lang}" var="AUTOMAT"/>
                <fmt:message key="MANUAL" bundle="${lang}" var="MANUAL"/>
                <option ${carFilter.isAutomat==AUTOMAT ? 'selected' : ''}>${AUTOMAT}</option>
                <option ${carFilter.isAutomat==MANUAL ? 'selected' : ''}>${MANUAL}</option>
                <option ${carFilter.isAutomat==ANY ? 'selected' : ''}>${ANY}</option>
            </select>
        </div>
        <div class="form-group col-md-3">
            <label for="isDiesel" class="control-label"><fmt:message key="FUEL" bundle="${lang}"/></label>
            <%--My alternative of select by using own tagLib and it's standard relization start--%>
            <my:select selected="${carFilter.isDiesel}" className="form-control" id="isDiesel">
                <option><fmt:message key="DIESEL" bundle="${lang}"/></option>
                <option><fmt:message key="GASOLINE" bundle="${lang}" /></option>
                <option>${ANY}</option>
            </my:select>
<%--
            <select class="form-control" name="isDiesel" id="isDiesel">
                <fmt:message key="DIESEL" bundle="${lang}" var="DIESEL"/>
                <fmt:message key="GASOLINE" bundle="${lang}" var="GASOLINE"/>
                <option ${carFilter.isDiesel==DIESEL ? 'selected' : ''}>${DIESEL}</option>
                <option ${carFilter.isDiesel==GASOLINE ? 'selected' : ''}>${GASOLINE}</option>
                <option ${carFilter.isDiesel==ANY ? 'selected' : ''}>${ANY}</option>
            </select>
--%>
            <%--My alternative of select by using own tagLib and it's standard relization end--%>
        </div>
        <div class="form-group col-md-3">
            <label for="hasCondition" class="control-label"><fmt:message key="AIR_CONDITION" bundle="${lang}"/></label>
            <select class="form-control" name="hasCondition" id="hasCondition">
                <fmt:message key="WITH_CONDITION" bundle="${lang}" var="WITH_CONDITION"/>
                <fmt:message key="NO_CONDITION" bundle="${lang}" var="NO_CONDITION"/>
                <option ${carFilter.hasCondition==WITH_CONDITION ? 'selected' : ''}>${WITH_CONDITION}</option>
                <option ${carFilter.hasCondition==NO_CONDITION ? 'selected' : ''}>${NO_CONDITION}</option>
                <option ${carFilter.hasCondition==ANY ? 'selected' : ''}>${ANY}</option>
            </select>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-md-3">
            <label for="price" class="control-label"><fmt:message key="MAX_PRICE" bundle="${lang}"/></label>

            <div>
                <input class="form-control" id="price" name="price" type="number" step="1" min="0" placeholder="10" value="${carFilter.price}"/>
            </div>
        </div>

        <div class="form-group col-md-3">
            <label for="beginDate" class="control-label"><fmt:message key="BEGIN_DATE" bundle="${lang}"/></label>
            <div >
                <input class="form-control" id="beginDate" name="beginDate" type="date"  value="${carFilter.beginDate}" />
            </div>
        </div>

        <div class="form-group col-md-3">
            <label for="endDate" class="control-label"><fmt:message key="END_DATE" bundle="${lang}"/></label>
            <div >
                <input class="form-control" id="endDate" name="endDate" type="date" value="${carFilter.endDate}"/>
            </div>
        </div>

        <div class="form-group col-md-3">
            <br/>
            <div>
                <button type="submit" class="btn btn-primary form-control" name="command" value="searchcar"><span class="glyphicon glyphicon-search" aria-hidden="true"></span> <fmt:message key="SEARCH" bundle="${lang}"/></button>
            </div>
        </div>
    </div>
</form>

<%--search panel end--%>

<%--search results start--%>

<c:if test="${ not empty cars}">
    <div class="row">
        <div class="table-responsive col-md-12 ">
            <table class="table table-striped table-bordered table-hover table-condensed">
                <tbody>
                <tr>
                    <th><fmt:message key="CAR_ID" bundle="${lang}"/></th>
                    <th><fmt:message key="CAR_MODEL" bundle="${lang}"/></th>
                    <th><fmt:message key="CAR_BRAND" bundle="${lang}"/></th>
                    <th><fmt:message key="CAR_CLASS" bundle="${lang}"/></th>
                    <th><fmt:message key="PRICE" bundle="${lang}"/></th>
                    <th><fmt:message key="TRANSMISSION" bundle="${lang}"/></th>
                    <th><fmt:message key="DIESEL" bundle="${lang}"/></th>
                    <th><fmt:message key="AIR_CONDITION" bundle="${lang}"/></th>
                    <th><fmt:message key="DOOR_QTY" bundle="${lang}"/></th>
                    <th><fmt:message key="CHOOSE" bundle="${lang}"/></th>
                </tr>
                <c:forEach items="${cars}" var="car">
                    <tr>
                        <td>${car.id}</td>
                        <td>${car.model}</td>
                        <td>${car.brand}</td>
                        <td>${car.className}</td>
                        <td>${car.price}</td>
                        <td><fmt:message key="${car.isAutomat}" bundle="${lang}"/></td>
                        <td><fmt:message key="${car.isDiesel}" bundle="${lang}"/></td>
                        <td><fmt:message key="${car.hasCondition}" bundle="${lang}"/></td>
                        <td>${car.doorQty}</td>
                        <td><a href="/reserve?id=${car.id}"><button type="submit" class="btn btn-primary form-control" name="command" value="searchcar"><span class="glyphicon glyphicon-pushpin" aria-hidden="true"></span> <fmt:message key="RESERVE" bundle="${lang}"/></button></a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <%--search results end--%>

<%--pagination logic start--%>
    <c:url var="firstUrl" value="/searchcar/pages/1" />
    <c:url var="lastUrl" value="/searchcar/pages/${totalPages}" />
    <c:url var="prevUrl" value="/searchcar/pages/${currentIndex - 1}" />
    <c:url var="nextUrl" value="/searchcar/pages/${currentIndex + 1}" />

    <div class="row">
        <div class="pagination col-md-12">
            <div class="btn-group" role="group" aria-label="...">
                    <%--start arrows block start--%>
                <c:choose>
                    <c:when test="${currentIndex == 1}">
                        <a href="#"><button type="button" class="btn btn-default disabled" >&lt;&lt;</button></a>
                        <a href="#"><button type="button" class="btn btn-default disabled" >&lt;</button></a>
                    </c:when>
                    <c:otherwise>
                        <a href="${firstUrl}"><button type="button" class="btn btn-default">&lt;&lt;</button></a>
                        <a href="${prevUrl}"><button type="button" class="btn btn-default">&lt;</button></a>
                    </c:otherwise>
                </c:choose>
                    <%--start arrows block end--%>
                    <%--number block start--%>
                <c:forEach var="i" begin="${beginIndex}" end="${endIndex}">
                    <c:url var="pageUrl" value="/searchcar/pages/${i}" />
                    <c:choose>
                        <c:when test="${i == currentIndex}">
                            <a href="${pageUrl}"><button type="button" class="btn btn-default active" ><c:out value="${i}" /></button></a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageUrl}"><button type="button" class="btn btn-default"><c:out value="${i}" /></button></a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                    <%--number block end--%>
                    <%--end arrows block start--%>
                <c:choose>
                    <c:when test="${currentIndex == totalPages}">
                        <a href="#"><button type="button" class="btn btn-default disabled">&gt;</button></a>
                        <a href="#"><button type="button" class="btn btn-default disabled">&gt;&gt;</button></a>
                    </c:when>
                    <c:otherwise>
                        <a href="${nextUrl}"><button type="button" class="btn btn-default">&gt;</button></a>
                        <a href="${lastUrl}"><button type="button" class="btn btn-default">&gt;&gt;</button></a>
                    </c:otherwise>
                </c:choose>
                    <%--end arrows block end--%>
            </div>
        </div>
    </div>
</c:if>
<%--pagination logic end--%>