$(function() {
	form.on('switch(autoRenew)', function(data) {
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/cert/setAutoRenew',
			data: {
				id: data.value,
				autoRenew: data.elem.checked ? 1 : 0
			},
			dataType: 'json',
			success: function(data) {

				if (data.success) {
					//location.reload();
				} else {
					layer.msg(data.msg);
				}
			},
			error: function() {
				layer.alert(commonStr.errorInfo);
			}
		});
	});

	layui.use('upload', function() {
		var upload = layui.upload;
		upload.render({
			elem: '#pemBtn',
			url: '/adminPage/main/upload',
			accept: 'file',
			done: function(res) {
				// 上传完毕回调
				if (res.success) {
					$("#pem").val(res.obj);
					var path = res.obj.split('/');
					$("#pemPath").html(path[path.length - 1]);
				}

			},
			error: function() {
				// 请求异常回调
			}
		});

		upload.render({
			elem: '#keyBtn',
			url: '/adminPage/main/upload',
			accept: 'file',
			done: function(res) {
				// 上传完毕回调
				if (res.success) {
					$("#key").val(res.obj);
					var path = res.obj.split('/');
					$("#keyPath").html(path[path.length - 1]);
				}
			},
			error: function() {
				// 请求异常回调
			}
		});
	});

	form.on('select(dnsType)', function(data) {
		checkDnsType(data.value);
	});


	form.on('select(type)', function(data) {
		checkType(data.value);
	});
	
	form.on('select(caType)', function(data) {
		$("#caKey").hide();
		if(data.value == 'zerossl'){
			$("#caKey").show();
		}
	});
})

function setCa(){
	$("#caType option:first").prop("selected", true);
	$("#eabKid").val("");
	$("#eabHmacKey").val("");
	
	layer.open({
		type: 1,
		title: "设置默认ca",
		area: ['500px', '400px'], // 宽高
		content: $('#caDiv')
	});
}

function setCaOver(){
	
	if($("#caType").val() == 'zerossl' ){
		if($("#eabKid").val() == '' || $("#eabHmacKey").val() == ''){
			layer.msg("未填写完整");
			return;
		}
	}
	
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/cert/setCa',
		data: $('#caForm').serialize(),
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				layer.alert(data.obj);
			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			layer.alert(commonStr.errorInfo);
		}
	});
}

function search() {
	$("#searchForm").submit();
}

function checkDnsType(value) {
	$("#ali").hide();
	$("#dp").hide();
	$("#cf").hide();
	$("#gd").hide();
	$("#hw").hide();

	$("#" + value).show();
}

function checkType(value) {
	$("#type0").hide();
	$("#type1").hide();
	$("#encryptionDiv").hide();

	if (value == 0) {
		$("#type0").show();
		$("#encryptionDiv").show();
	}
	if (value == 1) {
		$("#type1").show();
	}

	if (value == 2) {
		$("#encryptionDiv").show();
	}

	if (value == 3) {
		$("#encryptionDiv").show();
	}

	if (value == 4) {
		$("#encryptionDiv").show();
	}
}

function add() {
	$("#id").val("");
	$("#domain").val("");
	$("#type option:first").prop("selected", true);
	$("#dnsType option:first").prop("selected", true);
	$("#encryption option:first").prop("selected", true);
	$("#aliKey").val("");
	$("#aliSecret").val("");
	$("#dpId").val("");
	$("#dpKey").val("");
	$("#cfEmail").val("");
	$("#cfKey").val("");
	$("#gdKey").val("");
	$("#gdSecret").val("");

	$("#hwUsername").val("");
	$("#hwPassword").val("");
	$("#hwDomainName").val("");

	$("#pem").val("");
	$("#key").val("");
	$("#pemPath").html("");
	$("#keyPath").html("");

	$("#domain").attr("disabled", false);
	$("#domain").removeClass("disabled");
	$("#type").attr("disabled", false);
	$("#encryption").attr("disabled", false);
	$("#encryption").removeClass("disabled");

	checkType(0);
	checkDnsType('ali');

	form.render();
	showWindow(certStr.add);
}


function edit(id, clone) {
	$("#id").val(id);


	$.ajax({
		type: 'GET',
		url: ctx + '/adminPage/cert/detail',
		dataType: 'json',
		data: {
			id: id
		},
		success: function(data) {
			if (data.success) {

				var cert = data.obj;

				$("#domain").val(cert.domain);
				$("#type").val(cert.type);
				$("#dnsType").val(cert.dnsType != null ? cert.dnsType : 'ali');
				$("#encryption").val(cert.encryption != null ? cert.encryption : 'RSA');
				$("#aliKey").val(cert.aliKey);
				$("#aliSecret").val(cert.aliSecret);
				$("#dpId").val(cert.dpId);
				$("#dpKey").val(cert.dpKey);
				$("#cfEmail").val(cert.cfEmail);
				$("#cfKey").val(cert.cfKey);

				$("#gdKey").val(cert.gdKey);
				$("#gdSecret").val(cert.gdSecret);

				$("#hwUsername").val(cert.hwUsername);
				$("#hwPassword").val(cert.hwPassword);
				$("#hwDomainName").val(cert.hwDomainName);

				if (!clone) {
					$("#domain").attr("disabled", true);
					$("#domain").addClass("disabled");

					if (cert.pem != null && cert.pem != '' && cert.key != null && cert.key != '') {
						$("#type").attr("disabled", true);
						$("#encryption").attr("disabled", true);
						$("#encryption").addClass("disabled");
					} else {
						$("#type").attr("disabled", false);
						$("#encryption").attr("disabled", false);
						$("#encryption").removeClass("disabled");
					}

					$("#id").val(cert.id);
					$("#pem").val(cert.pem);
					$("#key").val(cert.key);
					var path = cert.pem.split('/');
					$("#pemPath").html(path[path.length - 1]);
					path = cert.key.split('/');
					$("#keyPath").html(path[path.length - 1]);
				} else {
					$("#domain").attr("disabled", false);
					$("#domain").removeClass("disabled");
					$("#encryption").attr("disabled", false);
					$("#encryption").removeClass("disabled");
					$("#type").attr("disabled", false);

					$("#id").val("");
					$("#pem").val("");
					$("#key").val("");
					$("#pemPath").html("");
					$("#keyPath").html("");
				}

				checkType(cert.type);
				checkDnsType(cert.dnsType != null ? cert.dnsType : 'ali');

				form.render();
				showWindow(certStr.edit);

			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			layer.alert(commonStr.errorInfo);
		}
	});
}

function showWindow(title) {
	layer.open({
		type: 1,
		title: title,
		area: ['1000px', '630px'], // 宽高
		content: $('#windowDiv')
	});
}

function addOver() {
	if ($("#domain").val() == "") {
		layer.msg(certStr.error1);
		return;
	}

	if ($("#type").val() == 0) {
		if ($("#dnsType").val() == 'ali') {
			if ($("#aliKey").val() == '' || $("#aliSecret").val() == '') {
				layer.msg(commonStr.IncompleteEntry);
				return;
			}
		}
		if ($("#dnsType").val() == 'dp') {
			if ($("#dpId").val() == '' || $("#dpKey").val() == '') {
				layer.msg(commonStr.IncompleteEntry);
				return;
			}
		}
		if ($("#dnsType").val() == 'cf') {
			if ($("#cfEmail").val() == '' || $("#cfKey").val() == '') {
				layer.msg(commonStr.IncompleteEntry);
				return;
			}
		}
		if ($("#dnsType").val() == 'gd') {
			if ($("#gdKey").val() == '' || $("#gdSecret").val() == '') {
				layer.msg(commonStr.IncompleteEntry);
				return;
			}
		}
		if ($("#dnsType").val() == 'hw') {
			if ($("#hwUsername").val() == '' || $("#hwPassword").val() == '' || $("#hwDomainName").val() == '') {
				layer.msg(commonStr.IncompleteEntry);
				return;
			}
		}
	}

	if ($("#type").val() == 1 && $("#pem").val() == $("#key").val()) {
		layer.msg(certStr.error5);
		return;
	}

	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/cert/addOver',
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
			url: ctx + '/adminPage/cert/del',
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


function issue(id, type) {
	if (confirm(certStr.confirm1)) {

		if (type == 4) {
			// CDN文件验证
			if (!confirm("请确认已在CDN配置好/.well-known/acme-challenge/路径回源站  ")) {
				return;
			}
		}

		layer.load();
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/cert/apply',
			data: {
				id: id,
				type: "issue"
			},
			dataType: 'json',
			success: function(data) {
				layer.closeAll();
				if (data.success) {
					if (data.obj == null) {
						layer.alert(certStr.applySuccess, function(index) {
							layer.close(index);
							location.reload();
						});
					} else {
						var html = ``;
						for (let i = 0; i < data.obj.length; i++) {
							var map = data.obj[i]
							html += `
								<tr>
									<td>${map.domain} <input type="hidden" name="domains" value="${map.domain}"> </td>
									<td>${map.type} <input type="hidden" name="types" value="${map.type}"> </td>
									<td>${map.value} <input type="hidden" name="values" value="${map.value}"> </td>
								</tr>
							`;
						}
						$("#notice").html(html);

						layer.open({
							type: 1,
							title: certStr.hostRecords,
							area: ['900px', '400px'], // 宽高
							content: $('#txtDiv')
						});

					}

				} else {
					layer.open({
						type: 0,
						area: ['810px', '400px'],
						content: data.msg
					});
				}
			},
			error: function() {
				layer.closeAll();
				layer.alert(commonStr.errorInfo);
			}
		});
	}
}


function renew(id) {

	if (confirm(certStr.confirm2)) {
		layer.load();
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/cert/apply',
			data: {
				id: id,
				type: "renew"
			},
			dataType: 'json',
			success: function(data) {
				layer.closeAll();
				if (data.success) {
					layer.alert(certStr.renewSuccess, function(index) {
						layer.close(index);
						location.reload();
					});
				} else {
					layer.open({
						type: 0,
						area: ['810px', '400px'],
						content: data.msg
					});
				}
			},
			error: function() {
				layer.closeAll();
				layer.alert(commonStr.errorInfo);
			}
		});
	}
}


function selectPem() {
	rootSelect.selectOne(function(rs) {
		$("#pem").val(rs);
		$("#pemPath").html(rs);
	})
}


function selectKey() {
	rootSelect.selectOne(function(rs) {
		$("#key").val(rs);
		$("#keyPath").html(rs);
	})
}

function download(id) {
	window.open(ctx + "/adminPage/cert/download?id=" + id);
}

function clone(id) {
	if (confirm(serverStr.confirmClone)) {
		edit(id, true);
	}
}


function getTxtValue(id) {
	if (confirm("是否获取申请参数")) {
		showLoad()
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/cert/getTxtValue',
			data: {
				id: id
			},
			dataType: 'json',
			success: function(data) {
				closeLoad();
				if (data.success) {
					var html = ``;

					for (let i = 0; i < data.obj.length; i++) {
						var map = data.obj[i]
						html += `
						<tr>
							<td>${map.domain} <input type="hidden" name="domains" value="${map.domain}"> </td>
							<td>${map.type} <input type="hidden" name="types" value="${map.type}"> </td>
							<td>${map.value} <input type="hidden" name="values" value="${map.value}"> </td>
						</tr>
					`;
					}

					$("#notice").html(html);

					layer.open({
						type: 1,
						title: certStr.hostRecords,
						area: ['900px', '400px'], // 宽高
						content: $('#txtDiv')
					});
				} else {
					layer.msg(data.msg);
				}
			},
			error: function() {
				closeLoad();
				layer.alert(commonStr.errorInfo);
			}
		});
	}
}


function setDnsServer() {
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/cert/getDnsServer',
		data: {
		},
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				var dnsServer = data.obj;

				layer.prompt({
					formType: 0,
					value: dnsServer,
					title: 'AcmeDNS配置'
				}, function(value, index, elem) {
					//alert(value); //得到value
					if (value.trim() == '') {
						alert("输入为空");
						return;
					}

					setDnsServerOver(value);

					layer.close(index);
				});

			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			layer.closeAll();
			layer.alert(commonStr.errorInfo);
		}
	});

}

function setDnsServerOver(value) {

	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/cert/setDnsServer',
		data: {
			value: value
		},
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				layer.msg("保存成功");
			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			layer.closeAll();
			layer.alert(commonStr.errorInfo);
		}
	});
}

function setCdnServer() {
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/cert/getCdnServer',
		data: {
		},
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				var cdnDomain = data.obj.cdnDomain;
				var cdnPort = data.obj.cdnPort;
				var cdnUrl = data.obj.cdnUrl;

				$("#cdnDomain").val(cdnDomain);
				$("#cdnPort").val(cdnPort);
				$("#cdnUrl").val(cdnUrl);

				layer.open({
					type: 1,
					title: "CDN文件效验服务器",
					area: ['500px', '400px'], // 宽高
					content: $('#cdnDiv')
				});
			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			layer.closeAll();
			layer.alert(commonStr.errorInfo);
		}
	});
}


function setCdnServerOver() {
	var cdnDomain = $("#cdnDomain").val();
	var cdnPort = $("#cdnPort").val();
	var cdnUrl = $("#cdnUrl").val();

	if (cdnDomain.trim() == '' || cdnPort.trim() == '' || cdnUrl.trim() == '') {
		layer.msg("未填写完整");
		return;
	}

	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/cert/setCdnServer',
		data: {
			cdnDomain: cdnDomain,
			cdnPort: cdnPort,
			cdnUrl: cdnUrl
		},
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				layer.closeAll();
				layer.msg("保存成功");
			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			layer.closeAll();
			layer.alert(commonStr.errorInfo);
		}
	});

}