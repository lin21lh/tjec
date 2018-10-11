<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.jbf.sys.user.po.SysUser" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

	<link rel="stylesheet" type="text/css" href="<%=path %>/component/jquery-easyui-1.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=path %>/component/jquery-easyui-1.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/common.css">
	<script type="text/javascript" src="<%=path %>/component/jquery-easyui-1.4/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="<%=path %>/component/jquery-easyui-1.4/jquery.supcan.easyui.min.js"></script>
	<script type="text/javascript" src="<%=path %>/component/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
	
	<script type="text/javascript" src="<%=path %>/component/jquery-easyui-1.4/jquery-extensions/datagrid-scrollview.js"></script>	
	<script type="text/javascript" src="<%=path %>/templates/common/util.js"></script>
	
	<script type="text/javascript">
		var contextpath = '<%=path%>';		
        var dz_regionid = '15';	
	</script>



