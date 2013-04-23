<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="submit">
  <spring:message code="label.submit" />
</c:set>

<!-- Submit Button -->
<div class="control-group">
  <label class="control-label"></label>
  <div class="controls">
    <form:button id="submitbutton" name="submitbutton"
      class="btn btn-success">${submit}</form:button>
  </div>
</div>