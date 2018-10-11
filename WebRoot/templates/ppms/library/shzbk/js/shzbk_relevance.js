var baseUrl = contextpath + "ppms/library/shzbk/ShzbkController/";
var urls = {
	queryUrl : baseUrl + "queryShzbkRelevance.do"
	
};

var shzbkRelevanceDataGrid;

/*用户关联页面初始化*/
$(function(){
	
	loadRelevanceShzbkGrid(urls.queryUrl);
});

/**
 * 加载datagrid列表
 * @param url
 * @return
 */
function loadRelevanceShzbkGrid(url) {
	shzbkRelevanceDataGrid = $("#shzbkRelevanceDataGrid").datagrid({
		fit : true,
		striped : false,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		queryParams: {
			orgname : $("#re_orgname").val(),
			linkperson : $("#re_linkperson").val(),
			socialid : $("#re_socialid").val(),
			status : $("#re_status").val()
		},
		url : urls.queryUrl,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "shzbkid",checkbox : true}  
        ,{field : "socialid",title : "主键",halign : 'center',width:190,sortable:true,hidden:true}
        ,{field : "usercode",title : "用户编码",halign : 'center',	width:80,sortable:true,hidden:true}
        ,{field : "username",title : "用户名称",halign : 'center',width:125,sortable:true,hidden:true}
        ,{field : "userpswd",title : "用户密码",halign : 'center',	width:80,sortable:true,hidden:true}
        ,{field : "orgname",title : "社会资本名称",halign : 'center',	width:200,sortable:true,}
        ,{field : "linkperson",title : "联系人",halign : 'center',width:100,sortable:true	}
        ,{field : "linkphone",title : "联系人号码",halign : 'center',	width:150	,sortable:true}
        ,{field : "categoryName",title : "所属行业",halign : 'center',	width:150,sortable:true	}
        ,{field : "status",title : "用户类别",halign : 'center',	width:120,sortable:true,
        	formatter : function(value,row,index){
		  		switch(value){
	    	  		case '1':
	    	  			return "用户录入";
	    	  			break;
	    	  		case '4':
	    	  			return "PPP中心录入";
	    	  			break;
		  		}
	  		}
        }
        ,{field : "remark",title : "备注",halign : 'center',	width:200,sortable:true }
        ,{field : "orgcode",title : "组织机构代码",halign : 'center',	width:100,sortable:true	,hidden:true}
        ,{field : "iscombo",title : "是否是联合体",halign : 'center',	width:120,sortable:true,hidden:true}
        ,{field : "applicationTime",title : "申请时间",halign : 'center',	width:120,sortable:true,hidden:true	}
        ,{field : "auditTime",title : "审批时间",halign : 'center',	width:100,sortable:true,hidden:true }
        ,{field : "auditUser",title : "审批人",halign : 'center',	width:150,sortable:true,hidden:true}
        ,{field : "categoryCode",title : "所属行业编码",halign : 'center',	width:150,sortable:true,hidden:true	}
        ,{field : "preferencesCode",title : "投资偏好编码",halign : 'center',	width:130,sortable:true,hidden:true	}
        ,{field : "preferencesName",title : "投资偏好名称",halign : 'center',	width:130,sortable:true,hidden:true	}
        ,{field : "relevanceUser",title : "关联用户id",halign : 'center',	width:130,sortable:true	,hidden:true}
        ,{field : "weixin",title : "微信标识",halign : 'center',	width:130,sortable:true	,hidden:true}
        ,{field : "iscomboName",title : "是否联合体",halign : 'center',	width:130,sortable:true	,hidden:true}
        ,{field : "relevanceUserName",title : "关联用户名称",halign : 'center',	width:130,sortable:true	,hidden:true}
        
       ] ]
	});
}

/**
 * 查询
 */
function shzbkRelevanceQuery(){
	var param = {
			orgname : $("#re_orgname").val(),
			linkperson : $("#re_linkperson").val(),
			socialid : $("#re_socialid").val(),
			status : $("#re_status").val()
		
		};
	shzbkRelevanceDataGrid.datagrid("load", param);
}
