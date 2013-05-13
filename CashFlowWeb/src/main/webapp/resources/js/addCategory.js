function saveCategory() {
	var name = $('#text_input').val();
	
	$.ajax({
		type : "POST",
		url : "/CashFlowWeb/add_category",
		data : "name=" + name,
		success : function(response) {
			alert(response);
//			
//			$("#select_category option").remove(); 
//            var options = '';
//            $.each(response, function(index, item) {
//                options += '<option value="' + item.categoryId + '">' + item.name + '</option>';
//                $("#select_category").html(options);
//            });
            
            $('#categoryModal').modal('hide');
		}
	
		
	});
}

function updateCategory() {
	var name = $('#text_input').val();
	
	$.ajax({
		type : "POST",
		url : "/CashFlowWeb/add_category",
		data : "name=" + name,
		success : function(response) {
			alert(response);
//			
//			$("#select_category option").remove(); 
//            var options = '';
//            $.each(response, function(index, item) {
//                options += '<option value="' + item.categoryId + '">' + item.name + '</option>';
//                $("#select_category").html(options);
//            });
            
            $('#categoryModal').modal('hide');
		}
	
		
	});
}