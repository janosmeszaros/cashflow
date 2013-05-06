<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page session="false"%>

<div class="container">

  <legend> ${legendLabel} </legend>

  <table id="datatable" class="table table-hover table-condensed">
    <thead>
      <tr>
        <th><spring:message code="label.deadline" /></th>
        <th><spring:message code="label.paid" /></th>
        <th><spring:message code="label.recurring_type" /></th>
        <th><spring:message code="label.category" /></th>
        <th><spring:message code="label.notes" /></th>
        <th><spring:message code="label.amount" /></th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="bill" items="${bills}">
        <tr>
          <td>${bill.deadlineDate}</td>
          
          <c:choose>
          <c:when test="${bill.payed}"><td>${bill.payedDate}</td></c:when>
          <c:otherwise><td><spring:message code="label.not_yet" /></td></c:otherwise>
          </c:choose>
          
          <td><spring:message code="label.${bill.recurringInterval}" /></td>
          <td>${bill.category}</td>
          <td>${bill.note}</td>
          <td><div class="text-right">${bill.amount}</div></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

</div>
