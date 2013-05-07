<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url var="url" value="/resources/" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<footer>
  <div class="footer footer-wrapper navbar navbar-fixed-bottom">
    <div class="container narrow row-fluid">
      <p>&copy; CashFlow 2013</p>
    </div>
  </div>
</footer>


<!-- Le javascript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="http://code.jquery.com/jquery.js"></script>
<script src="${url}js/bootstrap.min.js"></script>
<script src="${url}js/bootstrap-datepicker.js"></script>
<script src="${url}/js/jquery.dataTables.min.js"></script>
<script type="text/javascript">
	$('#datepicker').datepicker();
	$('#datatable').dataTable({
		"oLanguage" : {
			"oAria" : {
				"sSortAscending" : '${sort_ascending}',
				"sSortDescending" : '${sort_descending}'
			},
			"oPaginate" : {
				"sFirst" : '${first}',
				"sLast" : '${last}',
				"sNext" : '${next}',
				"sPrevious" : '${previous}'
			},
			"sEmptyTable" : '${emptyTable}',
			"sInfo" : '${info}',
			"sInfoEmpty" : '${info_empty}',
			"sInfoFiltered" : '${info_filtered}',
			"sInfoPostFix" : '${info_postfix}',
			"sInfoThousands" : '${info_thousands}',
			"sLengthMenu" : '${length_menu}',
			"sLoadingRecords" : '${loading_records}',
			"sProcessing" : '${processing}',
			"sSearch" : '${search}',
			"sZeroRecords" : '${zero_records}'
		}
	});
</script>
</body>

</html>

