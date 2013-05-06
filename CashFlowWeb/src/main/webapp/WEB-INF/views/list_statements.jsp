<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page session="false"%>

<div class="container">

  <legend> ${legendLabel} </legend>

  <table id="datatable" class="table table-hover table-condensed">
    <thead>
      <tr>
        <th><spring:message code="label.date" /></th>
        <c:if test="${is_recurring}">
          <th><spring:message code="label.recurring_type" /></th>
        </c:if>
        <th><spring:message code="label.category" /></th>
        <th><spring:message code="label.notes" /></th>
        <th><spring:message code="label.amount" /></th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="statement" items="${statements}">
        <tr>
          <td>${statement.date}</td>
          <c:if test="${is_recurring}"><td><spring:message code="label.${statement.recurringInterval}" /></td></c:if>
          <td>${statement.category}</td>
          <td>${statement.note}</td>
          <td><div class="text-right">${statement.amount}</div></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

</div>
