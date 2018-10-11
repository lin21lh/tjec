$.extend($.fn.datagrid.methods,{
	 outExcel : function(jq, param){ //includeHidden, title, excelVersion, numberCols,outExcelModel 1='selections'选中 、 2='currentPage'当前页
		return jq.each(function(){
			
			var datas = null;
			if (!param.outExcelModel || param.outExcelModel == '1') {
				datas = $(this).datagrid('getSelections');
				if (datas.length == 0) {
					easyui_warn("请选中需要导出的数据！");
					return ;
				}
			} else if (param.outExcelModel == '2') {
				var datas = $(this).datagrid('getRows');
				if (datas.length == 0) {
					easyui_warn("在数据列表中未有可导出的数据！");
					return ;
				}
			}
			
			
			var includeHidden = param.includeHidden;
			var title = param.title;
			var excelVersion = param.excelVersion;
			var numberCols = param.numberCols;
			
			if (!excelVersion)
				excelVersion = '';
			
			if (!numberCols)
				numberCols = new Array();
			
			var unFrozenCols = $(this).datagrid('getColumnFields', true);
			var frozenCols = $(this).datagrid('getColumnFields');
			
			var headers = new Array();
			for (var i=0; i<unFrozenCols.length; i++) {
				var header = {};
				var columnOption = $(this).datagrid('getColumnOption', unFrozenCols[i]);
				header.colname = columnOption.field;
				header.title = columnOption.title;
				header.isNumber = numberCols.indexOf(unFrozenCols[i]) == -1 ? false : true;
				header.isHidden = columnOption.hidden || !columnOption.title;
				header.width = columnOption.width;
				headers.push(header);
			}
			
			for (var j=0; j<frozenCols.length; j++) {
				var header = {};
				var columnOption = $(this).datagrid('getColumnOption', frozenCols[j]);
				header.colname = columnOption.field;
				header.title = columnOption.title;
				header.isNumber = numberCols.indexOf(unFrozenCols[i]) == -1 ? false : true;
				header.isHidden = columnOption.hidden || !columnOption.title;
				header.width = columnOption.width;
				headers.push(header);
			}
			
			//var datas = $(this).datagrid('getRows');
			
			var frm = document.createElement('form');
			frm.id = 'frmDummy';
			frm.name = 'frmDummy';
			frm.action = contextpath + 'base/excel/SysExcelOutController/outExcelCurrentPage.do';
			frm.method='POST';
			//frm.encoding = 'multipart/form-data';
			//frm.enctype = 'multipart/form-data';
			frm.target = 'frmDummyExceldatas';              
			//frm.className = 'x-hidden';
			
			var dataInput = document.createElement('input');
			dataInput.type = "hidden";
			dataInput.value = JSON.stringify(datas);
			dataInput.id = "datas";
			dataInput.name = "datas";
			
			var headerInput = document.createElement('input');
			headerInput.type = "hidden";
			headerInput.value = JSON.stringify(headers);
			headerInput.id = "headers";
			headerInput.name = "headers";

			
			var isHiddenInput = document.createElement('input');
			isHiddenInput.type = "hidden";
			isHiddenInput.value = includeHidden;
			isHiddenInput.id = "includeHidden";
			isHiddenInput.name = "includeHidden";
			
			var titleInput = document.createElement('input');
			titleInput.type = "hidden";
			titleInput.value = title;
			titleInput.id = "title";
			titleInput.name = "title";
			
			var excelVersionInput = document.createElement('input');
			excelVersionInput.type = "hidden";
			excelVersionInput.value = excelVersion;
			excelVersionInput.id = "excelVersion";
			excelVersionInput.name = "excelVersion";
			
			frm.appendChild(dataInput);
			frm.appendChild(headerInput);
			frm.appendChild(isHiddenInput);
			frm.appendChild(titleInput);
			frm.appendChild(excelVersionInput);
			
			document.body.appendChild(frm);
		    frm.submit();
		    dataInput.value = '';	
		    headerInput.value = '';
		    isHiddenInput.value = '';	
		    titleInput.value = '';
		    excelVersionInput.value = '';	
		});
	}
});