<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag"%>

<c:set var="categoryLabel">
  <spring:message code="label.category" />:
</c:set>

<!-- Category Selector -->
<div class="control-group">
  <label class="control-label">${categoryLabel}</label>
  <div class="controls ">
    <div class="input-append">
      <form:select path="category" itemLabel="name"
        items="${categories}" id="selectbasic" name="selectbasic"
        itemValue="categoryId" class="input-xlarge" />
      <span class="add-on btn" onclick="showAddCategory()"><i
        class="icon-plus-sign-alt"></i></span>
    </div>
  </div>
</div>


<script type="text/javascript">
<!--
	function showAddCategory() {
		$('#myModal').modal('toggle');
	}
//-->
</script>