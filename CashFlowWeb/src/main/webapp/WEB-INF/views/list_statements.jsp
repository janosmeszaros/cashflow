<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page session="false"%>

<div class="container">

  <legend> ${legendLabel} </legend>

  <table id="datatable" class="table table-hover table-condensed">
    <thead>
      <tr>
        <th> <spring:message code="label.date" /></th>
        <th><spring:message code="label.category" /></th>
        <th><spring:message code="label.notes" /></th>
        <th><spring:message code="label.amount" /></th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="statement" items="${statements}">
        <tr>
          <td>${statement.date}</td>
          <td>${statement.category}</td>
          <td>${statement.note}</td>
          <td><div class="text-right"> ${statement.amount}</div></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>



</div>
