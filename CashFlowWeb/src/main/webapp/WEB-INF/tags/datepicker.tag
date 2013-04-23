<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ attribute name="title" required="true" type="java.lang.String"%>
<%@ attribute name="path" required="true" type="java.lang.String"%>

<!-- Datepicker -->
<div class="control-group">
  <form:label path="${path}" class="control-label">${title}</form:label>
  <div class="controls">
    <div class="input-append date" id="datepicker"
      data-date="12-02-2013" data-date-format="dd-mm-yyyy">
      <form:input path="${path}" class="span2" size="16" type="text" value="12-02-2013"/>
      <span class="add-on"><i class="icon-calendar"></i></span>
    </div>
  </div>
</div>

