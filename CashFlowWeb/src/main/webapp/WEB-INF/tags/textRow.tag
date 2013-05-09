<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="path" required="true"%>
<%@ attribute name="label" required="true"%>

<div class="control-group">
  <label class="control-label">${label}</label>
  <div class="controls">
    <div class="input-append">

      <form:input path="${path}" id="text_input" name="text_input"
        class="span2"  type="text" required="required" />
    </div>

  </div>
</div>