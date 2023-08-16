$(function() {


})

function search() {
	$("#searchForm").submit();
}

function add() {
	$("#id").val("");
	$("#domain").val("");
	$("#certId option:first").prop("selected", true);
	$("#aliKey").val("");
	$("#aliSecret").val("");
	$("#autoDepley option:first").prop("selected", true);

	showWindow("添加节点");
}


function showWindow(title) {
	layer.open({
		type: 1,
		title: title,
		area: ['600px', '500px'], // 宽高
		content: $('#windowDiv')
	});
}

function addOver() {
	if ($("#domain").val() == '' || $("#aliKey").val() == '' || $("#aliSecret").val() == '') {
		layer.alert("未填写完整");
		return;
	}

	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/cdnNode/addOver',
		data: $('#addForm').serialize(),
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				location.reload();
			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			layer.alert(commonStr.errorInfo);
		}
	});
}


function del(id) {
	if (confirm(commonStr.confirmDel)) {
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/cdnNode/del',
			data: {
				id: id
			},
			dataType: 'json',
			success: function(data) {
				if (data.success) {
					location.reload();
				} else {
					layer.msg(data.msg)
				}
			},
			error: function() {
				layer.alert(commonStr.errorInfo);
			}
		});
	}
}


function edit(id) {
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/cdnNode/detail',
		data: {
			id: id
		},
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				var cdnNode = data.obj;

				$("#id").val(cdnNode.id);
				$("#domain").val(cdnNode.domain);
				$("#certId").val(cdnNode.certId);
				$("#aliKey").val(cdnNode.aliKey);
				$("#aliSecret").val(cdnNode.aliSecret);
				$("#autoDepley").val(cdnNode.autoDepley);

				showWindow("编辑节点");
			} else {
				layer.msg(data.msg)
			}
		},
		error: function() {
			layer.alert(commonStr.errorInfo);
		}
	});
}

function deploy(id) {
	if (confirm("确认部署?")) {
		showLoad();
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/cdnNode/deploy',
			data: {
				id: id
			},
			dataType: 'json',
			success: function(data) {
				closeLoad();
				if (data.success) {
					layer.msg("部署成功")
				} else {
					layer.msg(data.msg)
				}
			},
			error: function() {
				closeLoad();
				layer.alert(commonStr.errorInfo);
			}
		});
	}
}
