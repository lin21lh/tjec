
$(function() {	
	loadcheckbox("protype");
});

function uploadFileDiv(){
    var timeLong =$('#timeLong').val();
    var caseid = $('#caseid').val();
    showUploadifyModalDialogForMultiple(parent.$('#uploadifydiv'), "itemids", "XZFY_JZGL_JZ", caseid+"_"+timeLong, "filetd");
}

function loadcheckbox(boxid){
	var url = contextpath + "aros/jzgl/controller/CaseFileManageController/workflowCheckBox.do";
	$("#" + boxid).combobox({
		editable: false,
		url : url,
		valueField : "protype",
		textField : "proname",
		panelHeight : 'auto',
		panelMaxHeight :150,
		onChange: function(nv, ov) {
		}
	});
}