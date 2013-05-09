<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag"%>
<%@ page session="false"%>

<div class="container">

<c:url value="/add_bill" var="addUrl" />
  <form:form modelAttribute="bill" class="form-horizontal" action="${addUrl}" method="post">
    <fieldset>

      <!-- Form Name -->
      <legend>
        <spring:message code="navbar.add_bill" />
      </legend>

      <tag:amount_row currency="huf"></tag:amount_row>

      <c:set var="category">
        <spring:message code="label.category" />
      </c:set>

      <tag:category_row></tag:category_row>


      <c:set var="deadline">
        <spring:message code="label.deadline" />
      </c:set>
      <tag:datepicker path="deadlineDate" title="${deadline}:"></tag:datepicker>

      <tag:textarea></tag:textarea>

      <tag:recurring_selector withNoneOption="false"></tag:recurring_selector>

      <c:set var="submit">
        <spring:message code="label.submit" />
      </c:set>

      <tag:submit_button />

    </fieldset>
  </form:form>


</div>
