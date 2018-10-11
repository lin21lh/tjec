//设置全局变量
var datagrid_expert;
var qualificationdata=[];
/**专家管理
 * expert_init.js
 * */
var baseUrl = contextpath + "synthesize/expert/controller/ExpertDatabaseController/";
//路径
var urls = {
	qryExpert : baseUrl + "qryExpert.do",
	delExpert : baseUrl+ "delExpert.do",
	optExpertView : baseUrl+ "optExpertView.do",
	saveExpert : baseUrl+ "saveExpert.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do",
	qryExpertWorked : baseUrl+"qryExpertWorked.do",
	qryQualification : baseUrl+"qryQualification.do",
	qryAvoidUnitGrid : baseUrl+"qryAvoidUnitGrid.do",
	qual : contextpath+ "base/dic/dicElementValSetController/queryCbByElementcodeByCFilter.do?elementcode=QUALIFICATION&idColumn=code&textColumn=name"
	,uploadView : baseUrl + "uploadView.do",
	uploadFile :baseUrl+"uploadFile.do"
};
//类型
var types = {
		view : "view",
		add : "add",
		edit : "edit"
};
var isOrNot = {
		"0" : "否",
		"1" : "是"
	};
$(function() {
	//加载数据
	loaddatagrid();
	
	//加载查询条件
	loadqryconditon();
	
	$.getJSON(urls.qual+"&menuid="+menuid, function(json) {
		qualificationdata = json;
	});
});
function loaddatagrid(){
	datagrid_expert = $("#datagrid_expert").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		url : urls.qryExpert,
		queryParams: {
			dealStatus : 1,
			menuid : menuid
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_expert",
		showFooter : true,
		columns : [  [{	field : "expertid",checkbox : true, align:'left'}
		, {field : "name",title : "专家名称",halign : 'center',	width : 150,sortable:true,align:'left'}
		, {field : "code",title : "专家编码",halign : 'center',	width : 60,sortable:true, align:'left',hidden:true}
		, {field : "sex",title : "性别",halign : 'center',	width : 60,sortable:true, align:'left',hidden:true}
		, {field : "sexName",title : "性别",halign : 'center',	width : 60,sortable:true, align:'left'}
		, {field : "nation",title : "民族",halign : 'center',	width : 60,sortable:true, align:'left'}
		, {field : "birthday",title : "出生年月",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "idtype",title : "证件类别",halign : 'center',	width : 90,sortable:true, align:'left',hidden:true}
		, {field : "idtypeName",title : "证件类别",halign : 'center',	width : 90,sortable:true, align:'left'}
		, {field : "idcard",title : "证件号码",halign : 'center',	width : 140,sortable:true, align:'left'}
		, {field : "region",title : "所在地区",halign : 'center',	width : 150,sortable:true, align:'left'}
		, {field : "politicsStatus",title : "政治面貌",halign : 'center',	width : 60,sortable:true, align:'left',hidden:true}
		, {field : "politicsStatusName",title : "政治面貌",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "isEmergencyName",title : "是否是应急专家",halign : 'center',	width : 90,sortable:true, align:'left'}
		, {field : "isTrain",title : "近期是否接受过培训",halign : 'center',	width : 110,sortable:true, align:'left',hidden:true}
		, {field : "isTrainName",title : "近期是否接受过培训",halign : 'center',	width : 115,sortable:true, align:'left'}
		, {field : "isEmergency",title : "是否是应急专家",halign : 'center',	width : 90,sortable:true, align:'left',hidden:true}
		, {field : "expertType",title : "专家类型",halign : 'center',	width : 80,sortable:true, align:'left',hidden:true}
		, {field : "expertTypeName",title : "专家类型",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "highestDegree",title : "最高学历",halign : 'center',	width : 80,sortable:true, align:'left',hidden:true}
		, {field : "highestDegreeName",title : "最高学历",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "highestOffering",title : "最高学历",halign : 'center',	width : 80,sortable:true, align:'left',hidden:true}
		, {field : "highestOfferingName",title : "最高学位",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "major",title : "所学专业",halign : 'center',	width : 100,sortable:true, align:'left'}
		, {field : "graduateSchool",title : "毕业学校",halign : 'center',	width :110,sortable:true, align:'left'}
		, {field : "workingCondition",title : "工作状态",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "majorType",title : "从事专业类别",halign : 'center',	width : 80,sortable:true, align:'left',hidden:true}
		, {field : "majorTypeName",title : "从事专业类别",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "majorYear",title : "从事专业年限",halign : 'center',	width : 80,sortable:true, align:'right'}
		, {field : "industry",title : "所属行业",halign : 'center',	width : 80,sortable:true, align:'left',hidden:true}
		, {field : "industryName",title : "所属行业",halign : 'center',	width : 120,sortable:true, align:'left'}
		, {field : "duty",title : "职务",halign : 'center',	width : 120,sortable:true, align:'left'}
		, {field : "professionalTitle",title : "职称",halign : 'center',	width : 120,sortable:true, align:'left',hidden:true}
		, {field : "professionalTitleName",title : "职称",halign : 'center',	width : 100,sortable:true, align:'left'}
		, {field : "titleNumber",title : "职称编号",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "unitName",title : "工作单位",halign : 'center',	width : 120,sortable:true, align:'left'}
		, {field : "unitAddress",title : "工作地址",halign : 'center',	width : 200,sortable:true, align:'left'}
		, {field : "avoidUnit",title : "回避单位",halign : 'center',	width : 80,sortable:true, align:'left',hidden:true}
		, {field : "phoneNumber",title : "电话号码",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "wechat",title : "微信",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "qq",title : "qq号码",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "email",title : "邮件",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "homeTelephone",title : "家庭电话",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "homeAddress",title : "家庭地址",halign : 'center',	width : 200,sortable:true, align:'left'}
		, {field : "homePostcode",title : "家庭邮编",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "photo",title : "照片",halign : 'center',	width : 80,sortable:true, align:'left',hidden:true}
		, {field : "bidMajor",title : "评标专业",halign : 'center',	width : 250,sortable:true, align:'left',hidden:true}
		, {field : "bidMajorCode",title : "评标专业",halign : 'center',	width : 300,sortable:true, align:'left',hidden:true}
		, {field : "bidMajorName",title : "评标专业",halign : 'center',	width : 300,sortable:true, align:'left'}
		, {field : "bidArea",title : "评标区域",halign : 'center',	width : 300,sortable:true, align:'left',hidden:true}
		, {field : "bidAreaCode",title : "评标区域",halign : 'center',	width : 250,sortable:true, align:'left',hidden:true}
		, {field : "bidAreaName",title : "评标区域",halign : 'center',	width : 250,sortable:true, align:'left'}
		, {field : "qualification",title : "执业资格",halign : 'center',	width : 80,sortable:true, align:'left',hidden:true}
		, {field : "qualificationName",title : "执业资格",halign : 'center',	width : 250,sortable:true, align:'left'}
		, {field : "research",title : "个人研究及专业成就",halign : 'center',	width : 350,sortable:true, align:'left'}
		, {field : "hasBidProject",title : "曾经参与过的主要评标项目",halign : 'center',	width : 350,sortable:true, align:'left'}
		, {field : "isUse",title : "是否可用",halign : 'center',	width : 350,sortable:true, align:'left',hidden:true}
		, {field : "isUseName",title : "是否可用",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "createUserName",title : "创建人",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "createUser",title : "创建人",halign : 'center',	width : 80,sortable:true, align:'left',hidden:true}
		, {field : "createTime",title : "创建时间",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "updateUserName",title : "修改人",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "updateUser",title : "修改人",halign : 'center',	width : 80,sortable:true, align:'left',hidden:true}
		, {field : "updateTime",title : "修改时间",halign : 'center',	width : 80,sortable:true, align:'left'}
		]]
	});
}

function loadqryconditon(){
	
	$("#bidMajors").treeDialog({
		title :'选择评标专业',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'bidMajorCodes',
		prompt: "请选择评标专业",
		editable :false,
		multiSelect: true, //单选树
		dblClickRow: true,
		queryParams : {
			menuid : menuid
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		checkLevs: [9], //只选择3级节点
		elementcode : "BIDMAJOR",
		filters:{
			code: "评标专业代码",
			name: "评标专业名称"
		}
	});
	
	comboboxFuncByCondFilter(menuid,"expertTypes", "EXPERTTYPE", "code", "name");//专家类别
	comboboxFuncByCondFilter(menuid,"offering", "HIGHESTOFFERING", "code", "name");//最高学位
	comboboxFuncByCondFilter(menuid,"degree", "HIGHESTDEGREE", "code", "name");//最高学历
	comboboxFuncByCondFilter(menuid,"industrys", "CATEGORY", "code", "name");//所属行业
	comboboxFuncByCondFilter(menuid,"professional", "PROFESSIONALTITLE", "code", "name");//职称
	comboboxFuncByCondFilter(menuid,"qualifications", "QUALIFICATION", "code", "name");//执业资格
	
}

/**
 * 查询
 */
function qryExpert(){
	var param = {
			bidMajor : $("#bidMajors").treeDialog('getValue'),//所属行业
			expertType : $("#expertTypes").combobox("getValues").join(","),//专家类别
			highestOffering : $("#offering").combobox('getValues').join(","),//最高学位
			highestDegree : $("#degree").combobox('getValues').join(","),//最高学历
			industry : $('#industrys').combobox('getValues').join(","),//所属行业
			professionalTitle : $('#professional').combobox('getValues').join(","),//职称
			qualification : $('#qualifications').combobox('getValues').join(","),//执业资格
			name : $("#expertName").textbox('getValue'),
			menuid : menuid
			
		};
	
	$("#datagrid_expert").datagrid("load",param);
}

/**
 * 新增
 */
function addExpert(){
	var rows = datagrid_expert.datagrid("getSelections");
	/*if (rows.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}*/
	showModalDialog(550, "expert_form", types.add, datagrid_expert, "专家新增",urls.optExpertView+"?optFlag="+types.add, urls.saveExpert+"?optFlag="+types.add);
}

/**
 * 修改
 */
function editExpert(){
	var rows = datagrid_expert.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	showModalDialog(550, "expert_form", types.edit, datagrid_expert, "专家信息修改",urls.optExpertView+"?optFlag="+types.edit, urls.saveExpert+"?optFlag="+types.edit);
}

/**
 * 详情
 */
function detExpert(){
	var rows = datagrid_expert.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	showModalDialog(550, "expert_form", types.view, datagrid_expert, "专家信息详情",urls.optExpertView+"?optFlag="+types.view, urls.saveExpert+"?optFlag="+types.view);
}

/**
 * 窗口
 * @param height
 * @param form
 * @param operType
 * @param dataGrid
 * @param title
 * @param href
 * @param url
 */
function showModalDialog(height, form, operType, dataGrid, title, href, url) {

	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : 800,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var row = dataGrid.datagrid("getSelections")[0];
			
			
			if(operType == "edit" || operType == "add" ){
				if(operType == "add"){
					mdDialog.find('#headphoto').attr("src","");
				}else if(row.photo==""){
					mdDialog.find('#headphoto').attr("src","");
				}else{
					mdDialog.find('#headphoto').attr("src",row.photo);
				}
				//绑定上传附件按钮
				mdDialog.find('#btn').bind('click', 
						function(){   
					parent.$("#fileUpload").dialog({
							title : '上传头像',
							iconCls : 'icon-add',
							width : 380,
							modal: true,
							height : 250,
							href:urls.uploadView,
							onLoad : function() {
								
							}
						,buttons:[{
							text : "上传",
							iconCls : "icon-cancel",
							handler : function() {
								parent.parent.$("#file_upload_form").form("submit",{
									url : urls.uploadFile+"?saveFileName=head"+parent.$("#uuid").val()+"&fileType=jpg&maxSize=5",
									success : function(result) {
										parent.$.messager.progress('close');
										result = $.parseJSON(result);
										if (result.success) {
											//重新加载gride
											parent.$("#fileUpload").dialog('close');
											easyui_info(result.title);
											parent.$("#photo").val("../ppms/uploadfile/headphoto/head"+parent.$("#uuid").val()+".jpg");
											parent.$("#headphoto").attr("src","../ppms/uploadfile/headphoto/head"+parent.$("#uuid").val()+".jpg");  
										} else {
											parent.$.messager.alert('错误', result.title, 'error');
										}
									}
								});
							}
						}]
							
						});
					});
				
				comboboxFuncByCondFilter(menuid,"sex", "SEX", "code", "name", mdDialog);//性别
				comboboxFuncByCondFilter(menuid,"idtype", "IDTYPE", "code", "name", mdDialog);//证件类别
				comboboxFuncByCondFilter(menuid,"expertType", "EXPERTTYPE", "code", "name", mdDialog);//专家类型
				comboboxFuncByCondFilter(menuid,"politicsStatus", "POLITICSSTATUS", "code", "name", mdDialog);//政治面貌
				comboboxFuncByCondFilter(menuid,"isTrain", "ISORNOT", "code", "name", mdDialog);//是否参加培训
				comboboxFuncByCondFilter(menuid,"isUse", "ISORNOT", "code", "name", mdDialog);//是否可用
				comboboxFuncByCondFilter(menuid,"isEmergency", "ISEMERGENCY", "code", "name", mdDialog);//是否应急专家
				comboboxFuncByCondFilter(menuid,"highestDegree", "HIGHESTDEGREE", "code", "name", mdDialog);//最高学历
				comboboxFuncByCondFilter(menuid,"highestOffering", "HIGHESTOFFERING", "code", "name", mdDialog);//最高学位
				comboboxFuncByCondFilter(menuid,"majorType", "MAJORTYPE", "code", "name", mdDialog);//从事专业类别
				comboboxFuncByCondFilter(menuid,"industry", "CATEGORY", "code", "name", mdDialog);//所属行业
				comboboxFuncByCondFilter(menuid,"professionalTitle", "PROFESSIONALTITLE", "code", "name", mdDialog);//职称
				
				mdDialog.find("#bidMajorName").treeDialog({
					title :'选择评标专业',
					dialogWidth : 420,
					dialogHeight : 500,
					hiddenName:'bidMajorCode',
					prompt: "请选择评标专业",
					editable :false,
					multiSelect: true, //单选树
					dblClickRow: true,
					queryParams : {
						menuid : menuid
					},
					url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
					checkLevs: [1,2,3], //只选择3级节点
					elementcode : "BIDMAJOR",
					filters:{
						code: "评标专业代码",
						name: "评标专业名称"
					}
				});
				
				mdDialog.find("#bidAreaName").treeDialog({
					title :'选择评标区域',
					dialogWidth : 420,
					dialogHeight : 500,
					hiddenName:'bidAreaCode',
					prompt: "请选择评标区域",
					editable :false,
					multiSelect: true, //单选树
					dblClickRow: true,
					queryParams : {
						menuid : menuid
					},
					url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
					checkLevs: [1,2,3], //只选择3级节点
					elementcode : "BIDAREA",
					filters:{
						code: "评标区域代码",
						name: "评标区域名称"
					}
				});
				
			}
			
			if(operType == "edit" || operType=="view"){
				var f = parent.$.modalDialog.handler.find('#' + form);
				f.form("load", row);
				if(row.photo!=""){
					mdDialog.find('#headphoto').attr("src",row.photo);
				}
			}
			
			var expertid = mdDialog.find("#expertid").val();
			expertWorkedGrid(mdDialog,expertid);
			qualificationGrid(mdDialog,expertid);
			avoidUnitGrid(mdDialog,expertid);
			
			//判断是否是详情页面
			if(operType=="view"){
				//将所有的datdagrid的操作按钮置灰
				mdDialog.find("#worked_add").linkbutton('disable');
				mdDialog.find("#worked_del").linkbutton('disable');
				mdDialog.find("#qual_add").linkbutton('disable');
				mdDialog.find("#qual_del").linkbutton('disable');
				mdDialog.find("#qual_file").linkbutton('disable');
				mdDialog.find("#unit_add").linkbutton('disable');
				mdDialog.find("#unit_del").linkbutton('disable');
			}else{
				mdDialog.find("#worked_add").linkbutton('enable');
				mdDialog.find("#worked_del").linkbutton('enable');
				mdDialog.find("#qual_add").linkbutton('enable');
				mdDialog.find("#qual_del").linkbutton('enable');
				mdDialog.find("#qual_file").linkbutton('enable');
				mdDialog.find("#unit_add").linkbutton('enable');
				mdDialog.find("#unit_del").linkbutton('enable');
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
	});
};

/**
 * 按钮
 * @param operType
 * @param url
 * @param dataGrid
 * @param form
 * @returns
 */
function funcOperButtons(operType, url, dataGrid, form) {

	var buttons;
	if(operType=="view"){
		buttons = [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else if(operType=="add" || operType=="edit"){
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				//工作简历
				var o=parent.$.modalDialog.handler.find("#expertWorked");
				if(o.datagrid('getRows').length<1){
					easyui_warn("请至少录入一条工作简历信息！",null);
					return;
				}
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(o.datagrid('getRows'));
				parent.$.modalDialog.handler.find("#subStr_work").val(subStr);
				
				
				//执业资格
				var o=parent.$.modalDialog.handler.find("#qualification");
				/*if(o.datagrid('getRows').length<1){
					easyui_warn("请至少录入一条执业资格信息！",null);
					return;
				}*/
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(o.datagrid('getRows'));
				parent.$.modalDialog.handler.find("#subStr_qual").val(subStr);
				
				//回避单位
				var o=parent.$.modalDialog.handler.find("#avoidUnitGrid");
				if(o.datagrid('getRows').length<1){
					easyui_warn("请至少录入一条回避单位信息！",null);
					return;
				}
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(o.datagrid('getRows'));
				parent.$.modalDialog.handler.find("#subStr_unit").val(subStr);
				
				submitForm(url, form,operType);
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
	
	return buttons;
};

/**
 * 提交
 * @param url
 * @param form
 * @param operType
 */
function submitForm(url, form,operType) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	form.form("submit",{
			url : url,
			onSubmit : function() {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = form.form('validate');
				if (!isValid) {
					parent.$.messager.progress('close');
				}
				return isValid;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					//重新加载gride
					parent.$.modalDialog.handler.dialog('close');
					easyui_info(result.title);
					datagrid_expert.datagrid("reload");
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
			}
		});
};
var currentRow_work=-1;
//工作简历
function expertWorkedGrid(mdDialog,expertid){
	var grid = mdDialog.find("#expertWorked").datagrid({
        height: 200,
        title : '工作简历',
        url : urls.qryExpertWorked,
        queryParams : {expertid:expertid},
        rownumbers : true,
        singleSelect: true,
        idField: 'workedid',
        multiSort:false,
        showFooter : true,
        columns: [[//显示的列
                   { field: 'financeid',checkbox : true, title: '主键id', halign : 'center',width: 100, sortable: true}
                   ,{ field: 'name', title: '单位名称', halign : 'center',width: 210 ,editor: { type: 'validatebox', options: { required: true,missingMessage:"请输入单位名称",validType:{length:[0,40]}}}}
                   ,{ field: 'time', title: '起止日期', halign : 'center',width: 150 ,editor: { type: 'datebox', options: { required: true, editable:false,missingMessage:"请输入起止日期", validType:{length:[0,30]}}} }
                   ,{ field: 'duty', title: '职务',halign : 'center', width: 120,sortable: true,editor: { type: 'validatebox', options: { required: true,validType:{length:[0,15]}}}}
                   ,{ field: 'certifier', title: '证明人', halign : 'center',width: 100,sortable: true ,editor: { type: 'validatebox', options: {validType:{length:[0,15]}}} }
                   ,{ field: 'certifierPhone', title: '证明人电话',halign : 'center', width: 150,sortable: true ,editor: { type: 'validatebox',options: {validType:{length:[0,13]}}}}
                   ]],
       toolbar: [{
    	   id:"worked_add",
           text: '添加', iconCls: 'icon-add', handler: function () {
        	   
               var data = mdDialog.find("#expertWorked").datagrid("getData");//grid列表
        	   var total = data.total;//grid的总条数
        	   if(total!=0){
        		   if(mdDialog.find('#expertWorked').datagrid('validateRow', currentRow_work)){
        			   	mdDialog.find('#expertWorked').datagrid('endEdit', currentRow_work);
        				mdDialog.find("#expertWorked").datagrid('appendRow', {row: {}});//追加一行
                       	mdDialog.find("#expertWorked").datagrid("beginEdit", total);//开启编辑
                       	currentRow_work = mdDialog.find('#expertWorked').datagrid('getRows').length-1;
        		   }else{
        			   easyui_warn("当前存在正在编辑的行！");
        		   }
        	   }else{
                   	mdDialog.find("#expertWorked").datagrid('appendRow', {row: {}});//追加一行
                   	mdDialog.find("#expertWorked").datagrid("beginEdit", total);//开启编辑
                   	currentRow_work = mdDialog.find('#expertWorked').datagrid('getRows').length-1;
        	   }
        	   
        	   
           }
       },'-', {
    	   id:"worked_del",
           text: '删除', iconCls: 'icon-remove', handler: function () {
        	   var row = mdDialog.find("#expertWorked").datagrid('getSelections');
               if(row ==''){
               		easyui_warn("请选择要删除的数据！");
               		return;
               }else{
            	   var rowIndex= mdDialog.find("#expertWorked").datagrid('getRowIndex',row[0]);
            	   
	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
	                        if (r) {
	                       		mdDialog.find("#expertWorked").datagrid('deleteRow',rowIndex);
	                        }
	                  });
               }
           }
       }],
       onDblClickRow:function(rowIndex, rowData) {
    	 //结束上一行编辑，开启新行的编辑
    	   if(currentRow_work != rowIndex && mdDialog.find('#expertWorked').datagrid('validateRow', currentRow_work)){
    		   //结束上一行编辑
    		   mdDialog.find('#expertWorked').datagrid('endEdit', currentRow_work);
    		   //开启新行编辑
    			mdDialog.find("#expertWorked").datagrid('beginEdit', rowIndex);
    			currentRow_work = rowIndex;
    	   }else{
    		   easyui_warn("当前存在正在编辑的行！");
    	   }
       },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
    	   
    	   if( mdDialog.find('#expertWorked').datagrid('validateRow', currentRow_work)){
    		   //结束上一行编辑
    		   mdDialog.find('#expertWorked').datagrid('endEdit', currentRow_work);
    		   currentRow_work = -1;
    		   isEditFlag = false;
    	   }
       },onEndEdit:function(rowIndex, rowData){//结束编辑事件主要用来计算总和
    	   
       }
       
    });
	return grid;
}

//执业资格
var currentRow_qual=-1;
function qualificationGrid(mdDialog,expertid){
	var grid = mdDialog.find("#qualification").datagrid({
        height: 250,
        title : '执业资格',
        url : urls.qryQualification,
        queryParams : {expertid:expertid},
        rownumbers : true,
        singleSelect: true,
        idField: 'qualificationid',
        multiSort:false,
        showFooter : true,
        columns: [[//显示的列
                   { field: 'qualificationid',checkbox : true, title: '主键id', halign : 'center',width: 100, sortable: true}
                   ,{ field: 'enumCode', title: '执业资格名称', halign : 'center',width: 210 ,editor: { type : 'combobox',  
                       options : {  
                           url:urls.qual+"&menuid="+menuid,  
                           valueField: 'id',    
                           textField: 'text',    
                           panelHeight: 'auto',  
                           required: true ,
                           missingMessage:"请选择执业资格",
                           editable:false  
                           }  }
                   ,formatter: function(value){
                	   for(var i=0;i<qualificationdata.length;i++){
                		   if(qualificationdata[i].id==value){
                			   return qualificationdata[i].text;
                		   }
                	   }
                   }
                   }
                   ,{ field: 'expertid', title: '外键', halign : 'center',width: 210 ,hidden:true}
                   ,{ field: 'qualificationCode', title: '执业资格证书编号', halign : 'center',width: 150 ,editor: { type: 'validatebox', options: { required: true,missingMessage:"请输入执业资格证书编号",validType:{length:[0,50]}}} }
                   ,{ field: 'unit', title: '颁发单位',halign : 'center', width: 120,sortable: true,editor: { type: 'validatebox', options: { validType:{length:[0,60]}}}}
                   ,{ field: 'startTime', title: '颁发时间', halign : 'center',width: 100,sortable: true ,editor: { type: 'datebox',options: { editable:false,validType:{length:[0,30]}}} }
                   ,{ field: 'endTime', title: '有效截止时间',halign : 'center', width: 150,sortable: true ,editor: { type: 'datebox',options: { editable:false,validType:{length:[0,30]}}}}
                   ,{ field: 'filepath', title: '附件',halign : 'center', width: 150,sortable: true ,hidden:true}
                   ]],
       toolbar: [{
    	   id:"qual_add",
           text: '添加', iconCls: 'icon-add', handler: function () {
        	   var data = mdDialog.find("#qualification").datagrid("getData");//grid列表
        	   var total = data.total;//grid的总条数
        	   if(total!=0){
        		   if(mdDialog.find('#qualification').datagrid('validateRow', currentRow_qual)){
        			   	mdDialog.find('#qualification').datagrid('endEdit', currentRow_qual);
        				mdDialog.find("#qualification").datagrid('appendRow', {row: {}});//追加一行
                       	mdDialog.find("#qualification").datagrid("beginEdit", total);//开启编辑
                       	currentRow_qual = mdDialog.find('#qualification').datagrid('getRows').length-1;
        		   }else{
        			   easyui_warn("当前存在正在编辑的行！");
        		   }
        	   }else{
                   	mdDialog.find("#qualification").datagrid('appendRow', {row: {}});//追加一行
                   	mdDialog.find("#qualification").datagrid("beginEdit", total);//开启编辑
                   	currentRow_qual = mdDialog.find('#qualification').datagrid('getRows').length-1;
        	   }
           }
       },'-', {
    	   id:"qual_del",
           text: '删除', iconCls: 'icon-remove', handler: function () {
        	   var row = mdDialog.find("#qualification").datagrid('getSelections');
               if(row ==''){
               		easyui_warn("请选择要删除的数据！");
               		return;
               }else{
            	   var rowIndex = rowIndex= mdDialog.find("#qualification").datagrid('getRowIndex',row[0]);
	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
	                        if (r) {
	                       		mdDialog.find("#qualification").datagrid('deleteRow',rowIndex);
	                        }
	                  });
               }
            }
       },'-',{
    	   id:"qual_file",
    	   text: '附件管理', iconCls: 'icon-files', handler: function () {
    		   optFile();
    	   }
       }],
       onDblClickRow:function(rowIndex, rowData) {
    	 //结束上一行编辑，开启新行的编辑
    	   if(currentRow_qual != rowIndex && mdDialog.find('#qualification').datagrid('validateRow', currentRow_qual)){
    		   //结束上一行编辑
    		   mdDialog.find('#qualification').datagrid('endEdit', currentRow_qual);
    		   //开启新行编辑
    			mdDialog.find("#qualification").datagrid('beginEdit', rowIndex);
    			currentRow_qual = rowIndex;
    	   }else{
    		   easyui_warn("当前存在正在编辑的行！");
    	   }
       },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
    	   if( mdDialog.find('#qualification').datagrid('validateRow', currentRow_qual)){
    		   //结束上一行编辑
    		   mdDialog.find('#qualification').datagrid('endEdit', currentRow_qual);
    		   currentRow_qual = -1;
    		   isEditFlag = false;
    	   }
       },onEndEdit:function(rowIndex, rowData){//结束编辑事件主要用来计算总和
    	   
       }
       
    });
	return grid;
}

//回避单位
var currentRow_unit = -1;
function avoidUnitGrid(mdDialog,expertid){
	var grid = mdDialog.find("#avoidUnitGrid").datagrid({
        height: 250,
        title : '回避单位',
        url : urls.qryAvoidUnitGrid,
        queryParams : {expertid:expertid},
        rownumbers : true,
        singleSelect: true,
        idField: 'unitid',
        multiSort:false,
        showFooter : true,
        columns: [[//显示的列
                   { field: 'unitid',checkbox : true, title: '主键id', halign : 'center',width: 100, sortable: true}
                   ,{ field: 'name', title: '单位名称',halign : 'center', width: 220,sortable: true,editor: { type: 'validatebox', options: { required: true , missingMessage:"请输入单位名称",validType:{length:[0,60]}}}}
                   ,{ field: 'isWork', title: '是否工作单位', halign : 'center',width: 210 ,editor: { type : 'combobox',  
                       options : {  
                           data:[{id:0,text:'否'},{id:1,text:'是'}],
                           valueField: 'id',    
                           textField: 'text',    
                           panelHeight: 'auto',  
                           required: true ,  
                           missingMessage:"请选择是否是工作单位",
                           editable:false  
                           }  }
                   		,formatter: function(value){return isOrNot[value];}
                   }
                   ,{ field: 'avoidTime', title: '回避结束时间',halign : 'center', width: 150,sortable: true ,editor: { type: 'datebox',options: { editable:false,validType:{length:[0,30]}}}}
                   ]],
       toolbar: [{
    	   id:"unit_add",
           text: '添加', iconCls: 'icon-add', handler: function () {
        	   var data = mdDialog.find("#avoidUnitGrid").datagrid("getData");//grid列表
        	   var total = data.total;//grid的总条数
        	   if(total!=0){
        		   if(mdDialog.find('#avoidUnitGrid').datagrid('validateRow', currentRow_unit)){
        			   	mdDialog.find('#avoidUnitGrid').datagrid('endEdit', currentRow_unit);
        				mdDialog.find("#avoidUnitGrid").datagrid('appendRow', {row: {}});//追加一行
                       	mdDialog.find("#avoidUnitGrid").datagrid("beginEdit", total);//开启编辑
                       	currentRow_unit = mdDialog.find('#avoidUnitGrid').datagrid('getRows').length-1;
        		   }else{
        			   easyui_warn("当前存在正在编辑的行！");
        		   }
        	   }else{
                   	mdDialog.find("#avoidUnitGrid").datagrid('appendRow', {row: {}});//追加一行
                   	mdDialog.find("#avoidUnitGrid").datagrid("beginEdit", total);//开启编辑
                   	currentRow_unit = mdDialog.find('#avoidUnitGrid').datagrid('getRows').length-1;
        	   }
           }
       },'-', {
    	   id:"unit_del",
           text: '删除', iconCls: 'icon-remove', handler: function () {
        	   var row = mdDialog.find("#avoidUnitGrid").datagrid('getSelections');
               if(row ==''){
               		easyui_warn("请选择要删除的数据！");
               		return;
               }else{
            	   var rowIndex = rowIndex= mdDialog.find("#avoidUnitGrid").datagrid('getRowIndex',row[0]);
	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
	                        if (r) {
	                       		mdDialog.find("#avoidUnitGrid").datagrid('deleteRow',rowIndex);
	                        }
	                  });
               }
            }
       }],
       onDblClickRow:function(rowIndex, rowData) {
    	 //结束上一行编辑，开启新行的编辑
    	   if(currentRow_unit != rowIndex && mdDialog.find('#avoidUnitGrid').datagrid('validateRow', currentRow_unit)){
    		   //结束上一行编辑
    		   mdDialog.find('#avoidUnitGrid').datagrid('endEdit', currentRow_unit);
    		   //开启新行编辑
    			mdDialog.find("#avoidUnitGrid").datagrid('beginEdit', rowIndex);
    			currentRow_unit = rowIndex;
    	   }else{
    		   easyui_warn("当前存在正在编辑的行！");
    	   }
       },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
    	   if( mdDialog.find('#avoidUnitGrid').datagrid('validateRow', currentRow_unit)){
    		   //结束上一行编辑
    		   mdDialog.find('#avoidUnitGrid').datagrid('endEdit', currentRow_unit);
    		   currentRow_unit = -1;
    		   isEditFlag = false;
    	   }
       },onEndEdit:function(rowIndex, rowData){//结束编辑事件主要用来计算总和
    	   
       }
       
    });
	return grid;
}

/**
 * 删除
 */
function delExpert(){
	var rows = datagrid_expert.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条专家数据！");
		return;
	}
	var expertids = "";
	for(var i=0;i<rows.length;i++){
		if(i==0){
			expertids = rows[i].expertid;
		}else{
			expertids += ","+rows[i].expertid;
		}
	}
	parent.$.messager.confirm("删除确认", "请确保该专家没有在系统中使用过，否则会造成数据丢失，继续？", function(r) {
		if (r) {
			$.post(urls.delExpert, {
				expertids :expertids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_expert.datagrid("reload");
				});
			}, "json");
		}
	});
}
//附件管理
function optFile() {
	var rows = parent.$("#qualification").datagrid("getSelections");
	if(rows.length == 0){
		easyui_warn("请选择一条数据！");
		return;
	} else if (rows.length > 1) {
		easyui_warn("当且仅能选择一条数据！");
		return;
	}
	var keyid = rows[0].qualificationid;
	parent.$("#qualificationFile").dialog({
		title : '附件管理',
		width : 650,
		height : 380,
		href : contextpath + '/base/filemanage/fileManageController/entry.do?keyid=' + keyid + '&elementcode=QUALIFICATION',
		onBeforOpen: function(){
			console.log("open");
		},
		onLoad: function(){

		}
	});
}