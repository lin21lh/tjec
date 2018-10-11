function start() {
	var url = contextpath
			+ '/workflow/WfProcessInstanceController/startProcessByKey.do';
	var key = $('#key').val();
	$.post(url + "?pdefkey=" + key, {}, function(msg) {
		alert(msg.title);
	}, 'json');
}

function audit() {
	var url = contextpath
			+ '/workflow/WfProcessInstanceController/completeTask.do';
	var taskid = $('#taskid').val();
	if (taskid) {
		$.post(url, {
			taskid : taskid,
			outcome : $('#outcome').val()
		}, function(msg) {
			alert(msg.title);
		}, 'json');
	}
}