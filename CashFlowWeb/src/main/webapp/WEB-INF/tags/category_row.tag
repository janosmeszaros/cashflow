<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="categoryLabel">
  <spring:message code="label.category" />
</c:set>

<!-- Category Selector -->
<div class="control-group">
  <label class="control-label">${categoryLabel}</label>
  <div class="controls">
    <form:select path="category" itemLabel="name" items="${categories}" id="selectbasic" name="selectbasic" class="input-xlarge" />
  </div>
</div>
