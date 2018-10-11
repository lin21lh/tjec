function clickUploadDiv(elementcode){
	var fjkeyid =$('#fjkeyid').val();
	var id = $('#id').val();
	fjkeyid = id==""?fjkeyid:id;
	showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids",elementcode,fjkeyid);
}