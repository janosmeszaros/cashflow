<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ page session="false"%>

<div class="container">

  <form class="form-horizontal">
    <fieldset>

      <!-- Form Name -->
      <legend><spring:message code="navbar.add_bill"/></legend>

      <tag:amount_row></tag:amount_row>
      
      <c:set var="category"> <spring:message code="label.category"/> </c:set>
      <tag:category_row title="${category}"></tag:category_row>

      <!-- Button -->
      <div class="control-group">
        <label class="control-label">Deadline:</label>
        <div class="controls">
          <button id="datebutton" name="datebutton" class="btn btn-info">2013.04.18</button>
        </div>
      </div>

      <!-- Textarea -->
      <div class="control-group">
        <label class="control-label">Notes:</label>
        <div class="controls">
          <textarea id="notestextarea" name="notestextarea"></textarea>
        </div>
      </div>

      <!-- Multiple Radios (inline) -->
      <div class="control-group">
        <label class="control-label">Recurring type</label>
        <div class="controls">
          <label class="radio inline"> <input type="radio"
            name="radios" value="None" checked="checked"> None
          </label> <label class="radio inline"> <input type="radio"
            name="radios" value="Daily"> Daily
          </label> <label class="radio inline"> <input type="radio"
            name="radios" value="Weekly"> Weekly
          </label> <label class="radio inline"> <input type="radio"
            name="radios" value="Biweekly"> Biweekly
          </label> <label class="radio inline"> <input type="radio"
            name="radios" value="Monthly"> Monthly
          </label> <label class="radio inline"> <input type="radio"
            name="radios" value="Annually"> Annually
          </label>
        </div>
      </div>

      <c:set var="submit"> <spring:message code="label.submit"/> </c:set>
      <tag:button title="${submit}"> </tag:button>

    </fieldset>
  </form>


</div>