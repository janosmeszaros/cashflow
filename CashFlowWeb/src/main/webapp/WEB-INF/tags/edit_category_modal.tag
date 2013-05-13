<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag"%>

<div id="editCategoryModal" class="modal hide fade" tabindex="-1"
  role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="editCategoryModalLabel">
      <spring:message code="label.rename_category" />
    </h3>
  </div>
  <div class="modal-body">

    <c:url var="edit_category" value="/edit_category" />
    <form:form class="form-horizontal" action="${edit_category}"
      method="post" modelAttribute="category">
      <fieldset>

        <c:set var="nameLabel">
          <spring:message code="label.name" />
        </c:set>
        <tag:textRow label="${nameLabel}" path="name" />

        <c:set var="submit">
          <spring:message code="label.submit" />
        </c:set>

        <div class="control-group">
          <label class="control-label"></label>
          <div class="controls">
            <input type="button" id="submitbutton" name="submitbutton"
              class="btn btn-success" onClick="updateCategory()"
              value="${submit}">
          </div>
        </div>

      </fieldset>
    </form:form>

  </div>
</div>


<c:url var="url" value="/resources/" />
<script src="${url}js/addCategory.js" >
</script>
