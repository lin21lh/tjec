function clickUploadFileDiv(){
    var fjkeyid =$('#timeLong').val();
    var caseid = $('#caseid').val();
    showUploadifyModalDialogForMultiple(parent.$('#uploadifydiv'), "itemids", "XZFY_ZT_FJ", caseid+"_"+fjkeyid, "filetd");
}