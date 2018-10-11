<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>行政应诉</title>
<!--引用dynaload.js。后面的"?20"是让浏览器及时刷新的时间戳，请参见“常见问题解答”-->
<script type='text/javascript' src='$contextpath/component/supcan/binary/dynaload.js?21'></script>
<script type="text/javascript" src="$contextpath/templates/aros/tjfx/js/caseanalysis_admin.js"></script>

<%
 String contp = request.getContextPath();
  
%>

<script type="text/javascript">
  var menuid = "$menuid";

  function OnReady(id) {//控件产生后的处理
    AF.func("Build", "$contextpath/templates/aros/tjfx/xml/prodlogRpt.xml");//加载报表
	AF.func("SeparateView", "3\r\n3 "); //分屏冻结
	AF.func("SubscribeEvent", "SelChanged,EditChanged,DblClicked,Clicked");//预订事件
	AF.func("addEditAbleOnly", "droplist; checkbox;"); 
	AF.func("Calc", "");
  }
  
  
</script>
<style type="text/css">
  .cs-west{
    width: 50%;
    padding: 0;
  }
  .linkbutton_group{
    border-bottom: 1px solid #95B8E7;
  }
</style>
</head>
<body id="layout" class="easyui-layout">
   <div data-options="region:'center',split:false" >
        <div id="toolbar_center" class="toolbar_group" >
        <input type="hidden" id="menuid" name="menuid" value="$menuid" />
      <div class="toolbar_buttons">
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="projectQuery()" >查询</a>
        <span class='xian' ></span>
          <a href="#" id="addBtn" class="easyui-linkbutton" iconCls="icon-add" plain="true" onClick="projectAdd()">新增</a>
        <span class='xian' ></span>
          <a href="#" id="editBtn" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onClick="projectUpdate()">修改</a>
        <span class='xian' ></span>
          <a href="#" id="delBtn" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onClick="projectDelete()">删除</a>
        <span class='xian' ></span>
          <a href="#" id="viewBtn" class="easyui-linkbutton" iconCls="icon-detail" plain="true" onClick="projectView()">详情</a>        
            </div>
        <div id="qpanel1" class="easyui-panel collapsable-toolbar" collapsible="true" style="width:100%;border:0px;">
        <table  class="list" style='border:1px'>
                    <tr>
                <td>查询模块</td>
            </tr>     
                    
        </table>
        </div>
    </div>
     <div id="div_AR" style="background:#f6f6f6;margin-top:3px;width:99%;height:6%;z-index:-1;position:absolute;">     
    	<script>insertReport('AF', 'workmode=uploadRuntime;Border=none; Ruler=none; Rebar=none; BorderColor=#008888;PagesTabPercent=0;TabScrollBar=outofsize;')</script>	
 	 </div>
		
	 <script type="text/javascript">
	   $('#div_AR').height($(document).height()-60);	
	</script>
   </div>
   
</body>
</html>