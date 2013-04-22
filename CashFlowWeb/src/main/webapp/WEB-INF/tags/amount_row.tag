<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- Amount row-->
<div class="control-group">
  <label class="control-label">Amount:</label>
  <div class="controls">
    <div class="input-append">
      <form:input  path="amount" id="amounttext" name="amounttext" class="span2"
        placeholder="0.00" type="text" required="required"/> <span
        class="add-on">HUF</span>
    </div>

  </div>
</div>
