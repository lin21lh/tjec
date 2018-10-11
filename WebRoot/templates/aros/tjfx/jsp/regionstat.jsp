<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="reporthead.jsp"%>
<%@ include file="common-supcan-v14.jsp"%>
<script type="text/javascript"
	src="<%=path%>/templates/aros/tjfx/js/regionstat.js"></script>
<script type="text/javascript"
	src="<%=path%>/component/supcan/binary/dynaload.js?70"></script>
<script type="text/javascript"
	src="<%=path%>/templates/aros/tjfx/js/echarts.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=path%>/templates/aros/tjfx/js/echarts-all.js"
	type="text/javascript"></script>
<script type="text/javascript">
	var sessionId="JSESSIONID=<%=request.getSession().getId()%>;Path=<%=request.getContextPath()%>
	";
</script>

</head>
<body class="easyui-layout" border="false">

	<div data-options="region:'center',split:false">
		<div id="toolbar_center" class="toolbar_group">
			<input type="hidden" id="menuid" name="menuid" value="$menuid" />
			<div id="qpanel1" class="easyui-panel collapsable-toolbar"
				collapsible="true" style="width:100%;border:0px;">
				<table class="list" style='border:1px'>
					<tr>
						<td>行政区划:<input id="expresscom" class="easyui-textbox"
							name="expresscom" style="width:150px" /><span class='xian'></span>案件收文日期:<input
							id="receivedate" class="easyui-datebox" name="receivedate"
							style="width:150px" editable="false" />至<input id="receivedate"
							class="easyui-datebox" name="receivedate" style="width:150px"
							editable="false" /> <a href="#" class="easyui-linkbutton"
							iconCls="icon-search" plain="true" onClick="projectQuery()">查询</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-excel"
							plain="true" onClick="projectQuery()">导出</a>
						</td>

					</tr>


				</table>
			</div>
		</div>
		<div id="div_AR"
			style="background:#f6f6f6;margin-top:3px;width:99%;height:6%;z-index:-1;position:absolute;">
			<script>
				insertReport('AF', 'workmode=uploadRuntime;Border=none; Ruler=none; Rebar=none; BorderColor=#008888;PagesTabPercent=0;TabScrollBar=outofsize;')
			</script>
		</div>

		<script type="text/javascript">
			$('#div_AR').height($(document).height() - 60);
		</script>
</body>
</html>

