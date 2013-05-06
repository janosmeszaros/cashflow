<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag"%>
<%@ page session="false"%>

<c:url value="/add_statement" var="addUrl" />
<div class="container">

  <tag:add_category_modal></tag:add_category_modal>

  <form:form class="form-horizontal" modelAttribute="statement"
    action="${addUrl}" method="post">
    <fieldset>

      <c:choose>
        <c:when test="${is_income}">
          <legend>
            <spring:message code="navbar.add_income" />
          </legend>
        </c:when>
        <c:otherwise>
          <legend>
            <spring:message code="navbar.add_expense" />
          </legend>
        </c:otherwise>
      </c:choose>

      <tag:amount_row currency="huf"></tag:amount_row>

      <tag:category_row></tag:category_row>

      <c:set var="dateLabel">
        <spring:message code="label.date" />:
      </c:set>
      <tag:datepicker path="date" title="${dateLabel}"></tag:datepicker>

      <tag:textarea></tag:textarea>

      <c:choose>
        <c:when test="${is_income}">
          <tag:recurring_selector withNoneOption="true"></tag:recurring_selector>
        </c:when>
        <c:otherwise>
          <form:input type="hidden" path="recurringInterval" />
        </c:otherwise>
      </c:choose>

      <form:input type="hidden" path="type" />

      <tag:submit_button />

    </fieldset>
  </form:form>


</div>
