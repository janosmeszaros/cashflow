<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ page session="false"%>

<div class="container">

<%--   <form class="form-horizontal"> --%>
  <form:form class="form-horizontal" modelAttribute="statement" action="${addUrl}" method="post"> 
  
    <fieldset>

      <!-- Form Name -->
      <legend>Add Income</legend>

      <tag:amount_row></tag:amount_row>
      
      <c:set var="category"> <spring:message code="label.category"/> </c:set>
      <tag:category_row title="${category}"></tag:category_row>
      
      <tag:datepicker></tag:datepicker>
      <tag:textarea></tag:textarea>
      <tag:recurring_selector></tag:recurring_selector>
      
      <c:set var="submit"> <spring:message code="label.submit"/> </c:set>
      <tag:button title="${submit}"> </tag:button>

    </fieldset>
<%--   </form> --%>
</form:form>

</div>