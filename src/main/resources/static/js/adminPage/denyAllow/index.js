$(function() {
	
})



function addOver() {
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/denyAllow/addOver',
		data : $('#addForm').serialize(),
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				location.reload();
			} else {
				layer.msg(data.msg);
			}
		},
		error : function() {
			layer.alert(commonStr.errorInfo);
		}
	});
	
}