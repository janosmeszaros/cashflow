<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page session="false"%>

<div class="container">

  <legend> ${legendLabel} </legend>

  <p>
    <c:forEach var="statement" items="${statements}">
      <p>${statement.amount}, ${statement.category}, ${statement.date}, ${statement.note} </p><br />
    </c:forEach>
  </p>

</div>
