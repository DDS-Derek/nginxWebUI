var run = true;
var guid = new Date().getTime();

$(function() {
	setInterval(startTail, 1000);
});

function startTail() {
	if (run) {
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/log/tailCmd',
			data: {
				id: $("#id").val(),
				guid: guid
			},
			dataType: 'json',
			success: function(data) {
				if (data.success) {
					// 接收服务端的实时日志并添加到HTML页面中
					$("#log-container").html(data.obj);
					// 滚动条滚动到最低部
					if (data.obj != "") {
						//$("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
						window.scrollTo(0, document.body.scrollHeight);
					}
				}
			},
			error: function() {
				layer.alert(commonStr.errorInfo);
			}
		});
	}
}

function stopOrStart() {
	if (run) {
		run = false;
		$("#stopOrStart").html(loginStr.continue);
	} else {
		run = true;
		$("#stopOrStart").html(loginStr.pause);
	}
}
