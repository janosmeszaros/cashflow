<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page session="false"%>

<div class="container">

  <legend>
    <spring:message code="navbar.manage_categories" />
  </legend>

  <table id="datatable" class="table table-hover table-condensed">
    <thead>
      <tr>
        <th><spring:message code="label.category" /></th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="category" items="${categories}">
        <tr>
          <td>${category}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

</div>