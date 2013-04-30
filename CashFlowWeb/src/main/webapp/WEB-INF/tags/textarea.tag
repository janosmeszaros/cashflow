<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="title">
  <spring:message code="label.notes" />:
</c:set>

<!-- Notes Textarea -->
<div class="control-group">
  <label class="control-label">${title}</label>
  <div class="controls">
    <form:textarea  path="note" id="notestextarea" name="notestextarea"/>
  </div>
</div>
