<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag"%>
<%@ page session="false"%>

<c:url value="add_category" var="addUrl" />
<div class="container">

	<form:form class="form-horizontal" modelAttribute="category"
			action="${addUrl}" method="post">
		<fieldset>
			<legend> New Category </legend>

			<tag:textRow label="Name:" path="name" />
			<tag:submit_button />
	</fieldset>
	</form:form>

</div>