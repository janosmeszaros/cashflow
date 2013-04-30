function saveCategory() {
	var name = $('#text_input').val();
	
	$.ajax({
		type : "POST",
		url : "/CashFlowWeb/add_category",
		data : "name=" + name,
		success : function(response) {
			$('#categoryModal').modal('hide');
		}
	});
}