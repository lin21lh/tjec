function clickUploadDiv(elementcode, accfiletd)
{
	var fjkeyid = $('#fjkeyid').val();
	var trialid = $('#trialid').val();
	fjkeyid = trialid == "" ? fjkeyid : trialid;
	showUploadifyModalDialogForMultiple(parent.$('#uploadifydiv'), "itemids", elementcode, fjkeyid, accfiletd);
}