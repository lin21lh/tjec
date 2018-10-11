<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

	<% 
	   String reportid = request.getAttribute("reportid")==null?"0":request.getAttribute("reportid").toString();
	   String cloumsData = request.getAttribute("cloumsData")==null?"":request.getAttribute("cloumsData").toString();
       String searchid = request.getAttribute("searchid")==null?"0":request.getAttribute("searchid").toString();
       String searchtime = request.getAttribute("r_time")==null?"0":request.getAttribute("r_time").toString();
       String enterid = request.getAttribute("enterid")==null?"0":request.getAttribute("enterid").toString();
       String enterDim = request.getAttribute("enterDim")==null?"0":request.getAttribute("enterDim").toString();
       String regionid = request.getAttribute("regionid")==null?"0":request.getAttribute("regionid").toString();
       
        System.out.println("===============cloumsData:======== "+cloumsData);
      
	%>

	<script type="text/javascript">

		var r_id = '<%=reportid%>';		
	
		var cloums_data = [<%=cloumsData%>];	
		//暂时先将解析写死。以后扩展 syl	
		if(cloums_data!=null&&cloums_data.length==2){		
			for(var i=0;i<cloums_data[1].length;i++){	
			    var formulaNum  = cloums_data[1][i]['formularnum'];   		    
		  	    cloums_data[1][i]['formatter']=eval("(function(value,row,index){return formatNumber(value,"+formulaNum+");})");	   
			}
		 }	
		var searchid = [<%=searchid%>];
		var searchtime = '<%=searchtime%>';
		var init_regionid_dz = '';
		var enterid = '<%=enterid %>';		
        var init_regionid_dz_name='威海市';
        var enterDim = '<%=enterDim %>';   
        var region_id = '11';		
        var s_regionid = '<%=regionid%>';
        
        
        
 	</script>



