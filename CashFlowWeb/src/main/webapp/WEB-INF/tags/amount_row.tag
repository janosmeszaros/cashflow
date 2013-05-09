<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ attribute name="currency" required="true" type="java.lang.String"%>

<c:set var="title">
  <spring:message code="label.amount" />:
</c:set>


<!-- Amount row-->
<div class="control-group">
  <label class="control-label">${title}</label>
  <div class="controls">
    <div class="input-append">

      <form:input path="amount" id="amounttext" name="amounttext"
        class="span2" placeholder="0.00" type="text" required="required" />
      <span class="add-on"> <spring:message code="currency.${currency}" /> </span>
    </div>

  </div>
</div>
