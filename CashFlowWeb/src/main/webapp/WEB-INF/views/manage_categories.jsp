<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag"%>

<%@ page session="false"%>

<c:set var="tooltip">
  <spring:message code="tooltip.rename" />
</c:set>

<div class="container">

  <legend>
    <spring:message code="navbar.manage_categories" />
  </legend>

  <table id="datatable" class="table table-hover table-condensed">
    <thead>
      <tr>
        <th><spring:message code="label.category" /></th>
        <th><spring:message code="label.actions" /></th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="category" items="${categories}">
        <tr>
          <td>${category}</td>
          <td width="20%"><div class="btn" rel="tooltip"
              data-title="${tooltip}" data-trigger="hover"
              onclick="showEditCategory('${category.name}')">
              <span class="add-on"><i class="icon-edit"></i></span>
            </div></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <tag:edit_category_modal></tag:edit_category_modal>
</div>


<script type="text/javascript">
<!--
	function showEditCategory(name) {
		$('#text_input').val(name);
		$('#editCategoryModal').modal('toggle');
	}
//-->
</script>
