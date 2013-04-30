<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag"%>
<%@ page session="false"%>

<c:url value="/add_statement" var="addUrl" />
<div class="container">

  <!-- Modal -->
  <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog"
    aria-labelledby="myModalLabel" aria-hidden="true">

    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal"
        aria-hidden="true">×</button>
      <h3 id="myModalLabel">
        <spring:message code="label.add_category" />
      </h3>
    </div>
    <div class="modal-body">

      <c:url var="add_category" value="/add_category" />
      <form:form class="form-horizontal" action="${add_category}"
        method="post" modelAttribute="category">
        <fieldset>

          <c:set var="nameLabel">
            <spring:message code="label.name" />
          </c:set>
          <tag:textRow label="${nameLabel}" path="name" />
          <tag:submit_button />
        </fieldset>
      </form:form>

    </div>
  </div>
  <!-- end modal -->

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

      <%--             <c:if test="${is_income}"> --%>
      <%--               <tag:recurring_selector></tag:recurring_selector> --%>
      <%--             </c:if> --%>

      <form:input type="hidden" path="type" />

      <tag:submit_button />

    </fieldset>
  </form:form>


</div>
