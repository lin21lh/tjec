function clickUploadDiv(elementcode){
	var fjkeyid =$('#fjkeyid').val();
	var rcaseid = $('#rcaseid').val();
	fjkeyid = rcaseid == "" ? fjkeyid : rcaseid;
	showUploadifyModalDialog(parent.$('#uploadifydiv'), "itemids", elementcode, fjkeyid);
}