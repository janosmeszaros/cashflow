<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="title">
  <spring:message code="label.recurring_type" />
</c:set>
<c:set var="none">
  <spring:message code="label.none" />
</c:set>
<c:set var="daily">
  <spring:message code="label.daily" />
</c:set>
<c:set var="weekly">
  <spring:message code="label.weekly" />
</c:set>
<c:set var="biweekly">
  <spring:message code="label.biweekly" />
</c:set>
<c:set var="monthly">
  <spring:message code="label.monthly" />
</c:set>
<c:set var="annually">
  <spring:message code="label.annually" />
</c:set>

<!-- Multiple Radios (inline) -->
<div class="control-group">
  <label class="control-label">${title}</label>
  <div class="controls">
    <label class="radio inline"> <form:radiobutton
        path="interval" name="radios" value="None" checked="checked" />
      ${none}
    </label> <label class="radio inline"> <form:radiobutton
        path="interval" name="radios" value="Daily" /> ${daily}
    </label> <label class="radio inline"> <form:radiobutton
        path="interval" name="radios" value="Weekly" /> ${weekly}
    </label> <label class="radio inline"> <form:radiobutton
        path="interval" name="radios" value="Biweekly" /> ${biweekly}
    </label> <label class="radio inline"> <form:radiobutton
        path="interval" name="radios" value="Monthly" /> ${monthly}
    </label> <label class="radio inline"> <form:radiobutton
        path="interval" name="radios" value="Annually" /> ${annually}
    </label>
  </div>
</div>