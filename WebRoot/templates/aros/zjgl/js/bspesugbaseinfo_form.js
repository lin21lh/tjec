//默认加载
var timer;
$(function() {
	var caseid = $("#caseid").val();
	//showFileDiv($("#filetd"), false, "XZFY", caseid, "");
	$("#remarkTab").focus();
    loadxzfyReqDataGrid();
});

//加载grid列表
function loadxzfyReqDataGrid() {
	var url = contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/queryZTList.do";
	xzfyReqDataGrid = $("#ztdataTable").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:false,
		remoteSort:false,
		multiSort:true,
		queryParams:{
			caseid: caseid
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		columns:[[ 
		  {field:"groupname", title:"主题名称", halign:"center", width:'64%', sortable:true},
		  {field:"opttime", title:"提出日期", halign:"center", width:'36%', sortable:true}
        ]],
        onClickRow : function(rowIndex, rowData){
        	var url = "aros/zjgl/controller/BSpesugbaseinfoController/queryspesugbyZT.do?caseid="+ rowData.caseid + "&groupid="+ rowData.groupid;
        	$("#iframe").attr("src",url);
        },
        onLoadSuccess:function(){
        	var rowData = $('#ztdataTable').datagrid('getData').rows[0];
        	var url = "aros/zjgl/controller/BSpesugbaseinfoController/queryspesugbyZT.do?caseid="+ rowData.caseid + "&groupid="+ rowData.groupid;
        	$("#iframe").attr("src",url);
        }
	});
}
