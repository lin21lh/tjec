function clickUploadDiv(elementcode){
	var fjkeyid =$('#fjkeyid').val();
	var xmid = $('#xmid').val();
	fjkeyid = xmid==""?fjkeyid:xmid;
	showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids",elementcode,fjkeyid);
}