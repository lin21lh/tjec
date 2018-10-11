
function clickUploadDiv(elementcode, caseTraceFile){
	var fjkeyid =$('#fjkeyid').val();
	var id = $('#id').val();
	fjkeyid = id == "" ? fjkeyid : id;
	showUploadifyModalDialogForMultiple(parent.$('#uploadifydiv'), "itemids", elementcode, fjkeyid, caseTraceFile);
}