/**
 * 是否在矩形区域内
 * 
 * @param x
 *            点X坐标
 * @param y
 *            点Y坐标
 * @param x1
 *            区域左上角X坐标
 * @param y1
 *            区域左上角Y坐标
 * @param x2
 *            区域右下角X坐标
 * @param y2
 *            区域右下角Y坐标
 * @returns 是否在区域内
 */
WorkflowDesigner.prototype._inRectangle = function(x, y, x1, y1, x2, y2) {

	if (x1 - x2 == 0) {
		// 竖直
		if (y <= Math.max(y1, y2) && y > Math.min(y1, y2)) {
			if (Math.abs(x - x1) <= 2) {
				return true;
			}
		}
		return false;
	}

	var k = (y1 - y2) / (x1 - x2);
	var b = y1 - k * x1;
	var resy = k * x + b;
	var resx = (y - b) / k;
	// console.log("result suby =" + (resy - y));
	// console.log("result subx =" + (resx - x));

	if (Math.abs(resy - y) <= 5 || Math.abs(resx - x) <= 5) {
		if (y <= Math.max(y1, y2) + 3 && y >= Math.min(y1, y2) - 3
				&& x <= Math.max(x1, x2) + 3 && x >= Math.min(x1, x2) - 3) {
			return true;
		}
	}
	return false;
};

/**
 * 显示异常信息
 * 
 * @param msg
 */
WorkflowDesigner.prototype.warn = function(msg) {
	easyui_warn(msg);
};

/**
 * 显示提示信息
 * 
 * @param msg
 */
WorkflowDesigner.prototype.info = function(msg) {
	easyui_info(msg);
};

function xmlwriter() {
	this.xml = [];
	this.nodes = [];
	this.state = "";
	this.formatxml = function(str) {
		console.log(str);
		if (str)
			return str.replace(/&/g, "&amp;").replace(/\"/g, "&quot;").replace(
					/</g, "&lt;").replace(/>/g, "&gt;");
		return "";
	};
	this.beginnode = function(name) {
		if (!name)
			return;
		if (this.state == "beg")
			this.xml.push(">");
		this.state = "beg";
		this.nodes.push(name);
		this.xml.push("<" + name);
	};
	this.endnode = function() {
		if (this.state == "beg") {
			this.xml.push("/>");
			this.nodes.pop();
		} else if (this.nodes.length > 0)
			this.xml.push("</" + this.nodes.pop() + ">");
		this.state = "";
	};
	this.attrib = function(name, value) {
		if (this.state != "beg" || !name)
			return;
		this.xml.push(" " + name + "=\"" + this.formatxml(value) + "\"");
	};
	this.writestring = function(value) {
		if (this.state == "beg")
			this.xml.push(">");
		this.xml.push(this.formatxml(value));
		this.state = "";
	};
	this.node = function(name, value) {
		if (!name)
			return;
		if (this.state == "beg")
			this.xml.push(">");
		this.xml.push((value == "" || !value) ? "<" + name + "/>" : "<" + name
				+ ">" + this.formatxml(value) + "</" + name + ">");
		this.state = "";
	};
	this.close = function() {
		while (this.nodes.length > 0)
			this.endnode();
		this.state = "closed";
	};
	this.tostring = function() {
		return this.xml.join("");
	};
}

/**
 * 增加圆角矩形扩展
 * 
 * @param x
 * @param y
 * @param width
 * @param height
 * @param radius
 * @param fill
 * @param stroke
 */
CanvasRenderingContext2D.prototype.roundRect = function(x, y, width, height,
		radius, fill, stroke) {
	if (typeof stroke == "undefined") {
		stroke = true;
	}
	if (typeof radius === "undefined") {
		radius = 5;
	}
	this.beginPath();
	this.moveTo(x + radius, y);
	this.lineTo(x + width - radius, y);
	this.quadraticCurveTo(x + width, y, x + width, y + radius);
	this.lineTo(x + width, y + height - radius);
	this
			.quadraticCurveTo(x + width, y + height, x + width - radius, y
					+ height);
	this.lineTo(x + radius, y + height);
	this.quadraticCurveTo(x, y + height, x, y + height - radius);
	this.lineTo(x, y + radius);
	this.quadraticCurveTo(x, y, x + radius, y);
	this.closePath();

	if (fill) {
		this.fill();
	}
	if (stroke) {
		this.stroke();
	}
};

$
		.extend(
				$.fn.datagrid.defaults.editors,
				{
					searchbox : {
						init : function(container, options) {
							var input = $(
									'<input type="text" onfocus="privalige_onclick(this)" readonly>')
									.appendTo(container);
							return input;
						},
						destroy : function(target) {
							$(target).remove();
						},
						getValue : function(target) {
							return $(target).val();
						},
						setValue : function(target, value) {
							$(target).val(value);
						},
						resize : function(target, width) {
							$(target)._outerWidth(width);
						}
					},
					eventBox : {
						init : function(container, options) {
							var input = $(
									'<input type="text" onfocus="event_onclick(this)" readonly>')
									.appendTo(container);
							return input;
						},
						destroy : function(target) {
							$(target).remove();
						},
						getValue : function(target) {
							return $(target).val();
						},
						setValue : function(target, value) {
							$(target).val(value);
						},
						resize : function(target, width) {
							$(target)._outerWidth(width);
						}
					},
					assignBox : {
						init : function(container, options) {
							var input = $(
									'<input type="text" onfocus="assignBox_onclick(this)" readonly>')
									.appendTo(container);
							return input;
						},
						destroy : function(target) {
							$(target).remove();
						},
						getValue : function(target) {
							return $(target).val();
						},
						setValue : function(target, value) {
							$(target).val(value);
						},
						resize : function(target, width) {
							$(target)._outerWidth(width);
						}
					}

				});

function privalige_onclick(ele) {
	parent.$.modalDialog({
		title : '工作流角色选择',
		width : 500,
		height : 400,
		href : 'workflow/WfProcessDefinitionController/wfPrivilegeDialog.do',
		onLoad : function() {
			roleTreeSelection = null;
			var roletree = parent.$.modalDialog.handler.find('#priv_roletree');
			roletree.tree({
				url : 'sys/SysRoleController/query.do',
				onClick : function(node) {
					roleTreeSelection = {
						text : node.text,
						id : node.id
					};
				}
			});
		},
		buttons : [ {
			text : "确定",
			iconCls : "icon-ok",
			handler : function() {
				if (roleTreeSelection == null) {
					easyui_warn('请选择一个角色!');
					return;
				}
				var handle = getWorkflowDesigner();
				$(ele).val(roleTreeSelection.text);
				parent.$.modalDialog.handler.dialog('close');

				handle._propEndEdit();
				if (handle._activeObjectName) {
					var acti = handle._actiMap[handle._activeObjectName];
					acti.privilege = roleTreeSelection.text;
					acti.privilegeid = roleTreeSelection.id;
				}
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

function event_onclick(ele) {

	var sel = getTreeSelect();

	var par = $('#wftree').tree('getParent', sel.target);

	parent.$
			.modalDialog({
				title : '工作流事件对应操作设置',
				width : 300,
				height : 300,
				href : 'workflow/WfProcessDefinitionController/wfOpSelDialog.do',
				onLoad : function() {
					opGridSelection = null;
					var sel_grid = parent.$.modalDialog.handler
							.find('#oper_select_grid');
					sel_grid
							.datagrid({
								url : 'workflow/WfProcessDefinitionController/findWorkflowOpdef.do'
										+ '?key=' + par.key,
								columns : [ [ {
									field : 'ck',
									checkbox : true
								}, {
									field : 'name',
									title : '操作名称',
									width : 210
								} ] ],
								onSelect : function(ridx, data) {
									opGridSelection = {
										code : data.code,
										name : data.name
									};
								},
								rownumbers : true,
								singleSelect : true,
								fit : true
							});
				},
				buttons : [
						{
							text : "确定",
							iconCls : "icon-ok",
							handler : function() {
								if (opGridSelection == null) {
									easyui_warn('请选择一个操作!');
									return;
								}
								$(ele).val(opGridSelection.name);
								parent.$.modalDialog.handler.dialog('close');
								var handle = getWorkflowDesigner();
								handle._propEndEdit();
								if (handle._activeObjectName) {
									var acti = handle._actiMap[handle._activeObjectName];
									// 判断是start event还是end event
									if (eventFlag == 'startEvent'
											|| eventFlag == "endEvent") {
										acti[eventFlag] = opGridSelection.name;
									} else {
										alert('错误的事件类型:' + eventFlag);
									}
								} else if (handle._activeLine) {
									var from_ = handle._activeLine.from;
									for ( var i in from_.transition) {
										if (from_.transition[i] instanceof Function)
											continue;

										if (from_.transition[i].to == activeLine.to) {
											from_.transition[i].event = opGridSelection.name;
										}
									}
								}
							}
						},
						{
							text : "清空",
							iconCls : "icon-reload",
							handler : function() {

								$(ele).val("");
								var handle = getWorkflowDesigner();
								parent.$.modalDialog.handler.dialog('close');
								handle._propEndEdit();
								if (handle._activeObjectName) {
									var acti = handle._actiMap[handle._activeObjectName];
									// 判断是start event还是end event
									if (eventFlag == 'startEvent'
											|| eventFlag == "endEvent") {
										acti[eventFlag] = undefined;
									} else {
										alert('错误的事件类型:' + eventFlag);
									}
								} else if (handle._activeLine) {
									var from_ = handle._activeLine.from;
									for ( var i in from_.transition) {
										if (from_.transition[i] instanceof Function)
											continue;
										if (from_.transition[i].to == activeLine.to) {
											from_.transition[i].event = undefined;
										}
									}
								}
							}
						}, {
							text : "关闭",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.modalDialog.handler.dialog('close');
							}
						} ]
			});
}

function assignBox_onclick(ele) {

	var sel = getTreeSelect();

	var par = $('#wftree').tree('getParent', sel.target);

	parent.$
			.modalDialog({
				title : '工作流受托人属性参数设置',
				width : 400,
				height : 300,
				href : 'workflow/WfProcessDefinitionController/wfAssignBoxSelector.do',
				onLoad : function() {
					// var assignGrid =
					// parent.$.modalDialog.handler.find('#assignGrid');
					// assignGrid.hide();
					var assignTree = parent.$.modalDialog.handler
							.find('#assignTree');
					// sel_grid
					// .datagrid({
					// url :
					// 'workflow/WfProcessDefinitionController/findWorkflowOpdef.do'
					// + '?key=' + par.key,
					// columns : [ [ {
					// field : 'ck',
					// checkbox : true
					// }, {
					// field : 'name',
					// title : '操作名称',
					// width : 210
					// } ] ],
					// onSelect : function(ridx, data) {
					// opGridSelection = {
					// code : data.code,
					// name : data.name
					// };
					// },
					// rownumbers : true,
					// singleSelect : true,
					// fit : true
					// });

					assignTree.tree({
						url : 'sys/SysRoleController/query.do',
						animate : true,
						checkbox : true,
						cascadeCheck : false
					});
				},
				buttons : [
						{
							text : "确定",
							iconCls : "icon-ok",
							handler : function() {

								var assignTree = parent.$.modalDialog.handler
										.find('#assignTree');
								var select = assignTree.tree('getChecked');
								if(select.length==0){
									easyui_warn("请至少选择一个角色！");
									return;
								}
								var value1="",value2="";
								
								for(var i = 0 ;i<select.length;i++){
									value1+=select[i].id+",";
									value2+=select[i].text+",";
								}
								if(value1.length>0){
									value1=value1.substring(0,value1.length-1);
								}
								if(value2.length>0){
									value2=value2.substring(0,value2.length-1);
								}
								$(ele).val(value2+"##"+value1);
								parent.$.modalDialog.handler.dialog('close');
								var handle = getWorkflowDesigner();
								handle._propEndEdit();

								var acti = handle._actiMap[handle._activeObjectName];
								// 判断是start event还是end event

								acti['assignmentArgs'] = value2+"##"+value1;

							}
						},
						{
							text : "清空",
							iconCls : "icon-reload",
							handler : function() {

								$(ele).val("");
								var handle = getWorkflowDesigner();
								parent.$.modalDialog.handler.dialog('close');
								handle._propEndEdit();
								acti[assignmentArgs] = "";
							}
						}, {
							text : "关闭",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.modalDialog.handler.dialog('close');
							}
						} ]
			});
}
