/**
 * 构建模型
 */
WorkflowDesigner.prototype._buildModel = function(_modelBuiltCallBack) {
	this._parsePrcocessAndDiagram();
	this._loadExtendedAttributes(_modelBuiltCallBack);
	// _modelBuiltCallBack();

};
/**
 * 由jpdl解析流程属性以及流程图表
 */
WorkflowDesigner.prototype._parsePrcocessAndDiagram = function() {
	var text = this.jpdl;

	// 解析xml文档
	var xmlDoc = null;
	var domParser = new DOMParser();
	xmlDoc = domParser.parseFromString(text, 'text/xml');
	var agent = navigator.userAgent.toLowerCase();

	// 解析流程版本的属性
	var process = null;
	if (agent.indexOf("chrome") > 0 || agent.indexOf("webkit") > 0)
		process = xmlDoc.childNodes[0];
	else
		process = xmlDoc.children[0];
	var proc_attr = process.attributes;
	// this.processInfo = {};
	for ( var k in proc_attr) {
		var attr = proc_attr[k];
		if (attr instanceof Function)
			continue;

		this.processInfo[attr.name] = attr.value;
	}
	this.processInfo.description = undefined;

	// 解析流程图节点的属性
	var nodes = process.children;
	this.actiArray = new Array();
	for ( var i in nodes) {
		var node = nodes[i];
		if (node instanceof Function)
			continue;

		this._parseNode(node);
	}

	// 建立各节点（shape）的中心坐标
	for ( var i in this.actiArray) {
		var shape = this.actiArray[i];
		if (shape instanceof Function)
			continue;
		var locs = shape.g.split(",");

		this.actiArray[i].x = +locs[0];
		this.actiArray[i].y = +locs[1];
		this.actiArray[i].w = +locs[2];
		this.actiArray[i].h = +locs[3];

		this.actiArray[i].centerX = +locs[0] + (+locs[2]) / 2;
		this.actiArray[i].centerY = +locs[1] + (+locs[3]) / 2;
	}

	/***************************************************************************
	 * 建立各线的路径 对transition建立如下结构 transition.path={ orgX, orgY, destX, destY,
	 * points:[ {x0,y0}, {x1,y1}, ... ] }
	 **************************************************************************/
	this.actiMap = new Array();
	for ( var i in this.actiArray) {
		var tempNd = this.actiArray[i];
		if (tempNd instanceof Function)
			continue;

		if (tempNd.name) {
			this.actiMap[tempNd.name] = tempNd;
		}
	}

	for ( var i in this.actiArray) {
		var node = this.actiArray[i];
		if (node instanceof Function)
			continue;
		for ( var j in node.transition) {
			var tran = node.transition[j];
			if (tran instanceof Function)
				continue;
			this._buildTransitionStruct(tran, node);
		}
	}
};
/**
 * 加载流程版本中的各节点的扩展属性
 * 
 * @param _modelBuiltCallBack
 *            加载完后的回调函数
 */
WorkflowDesigner.prototype._loadExtendedAttributes = function(
		_modelBuiltCallBack) {

	var handle = this;
	$.post(this.urls.workflowExtAttrsUrl, {
		key : this.processInfo.key,
		version : this.processInfo.version
	}, function(result) {
		if (result instanceof Array) {
			handle.extAttrCache = result;

			handle._fillExtAttrsToActiMap();
			// 执行回调
			_modelBuiltCallBack();
		} else {
			handle.extAttrCache = [];
			handle.warn("工作流流程图节点扩展属性加载过程异常！");
		}

	}, "JSON");
};

WorkflowDesigner.prototype._parseNode = function(node) {
	if (!node.tagName) {
		return;
	}
	var nd = {};
	nd.tagName = node.tagName;
	var attrs = node.attributes;
	for ( var i in attrs) {
		if (attrs[i].name) {
			nd[attrs[i].name] = attrs[i].value;
		}
	}
	nd.transition = new Array();
	var children = node.children;
	for ( var i in children) {
		if (children[i] instanceof Function)
			continue;
		switch (children[i].tagName) {
		case 'transition':
			var trans = {};
			for ( var j in children[i].attributes) {
				if (children[i].attributes[j] instanceof Function)
					continue;
				if (children[i].attributes[j].name) {
					trans[children[i].attributes[j].name] = children[i].attributes[j].value;
				}
			}
			if (children[i].children) {
				for ( var k = 0; k < children[i].children.length; k++) {
					var cnode = children[i].children[k];
					if (cnode.tagName == 'condition') {
						trans.expr = cnode.attributes[0].value;
					}
				}
			}
			// 不再需要解析
			// this._dealTransitionEvent(trans, children[i]);
			nd.transition.push(trans);
			break;

		// 不再需要解析
		// case 'handler':
		// if (nd.tagName == 'decision') {
		// nd.handler = children[i].attributes[0].value;
		// }
		// break;

		// case 'assignment-handler':
		// if (nd.tagName == 'task') {
		// nd.assignmentHandler = children[i].attributes[0].value;
		// }
		// break;
		case 'on':
			this._dealOnEvent(nd, children[i]);
			break;
		case 'description':
			nd.description = children[i].textContent;
			break;

		default:
			break;
		}
	}
	this.actiArray.push(nd);
};

WorkflowDesigner.prototype._dealOnEvent = function(activity, xmlNode) {
	// 事务名
	var event = null;
	var attrs = xmlNode.attributes;
	for ( var i in attrs) {
		if (attrs[i] instanceof Function)
			continue;
		if (attrs[i].name == 'event') {
			event = attrs[i].value;
			break;
		}
	}
	var event_listener = this._getChildrenNode(xmlNode, 'event-listener');
	if (event_listener) {
		var field = this._getChildrenNode(event_listener, "field");
		if (field) {
			var valueNode = this._getChildrenNode(field, "string");
			if (valueNode) {
				var stringValue = this._getAttribute(valueNode, 'value');
				var args = this._stringToMapObj(stringValue);

				var otherArg = new Array();
				for ( var key in args) {
					if (args[key] instanceof Function)
						continue;
					// 取操作参数
					if (key == 'operation') {
						if (args['operation'] == 'null') {
							activity[event + 'Event'] = null;
						} else {
							activity[event + 'Event'] = args['operation'];
						}
					} else {
						// 其它参数
						otherArg.push(key + ":" + args[key]);
					}
				}
				activity[event + 'Args'] = otherArg.join(";");

			}
		}

	}
};
/**
 * 处理迁称线上的事件
 * 
 * @param tran
 * @param xmlNode
 */
// WorkflowDesigner.prototype._dealTransitionEvent = function(tran, xmlNode) {
// var event_listener = this._getChildrenNode(xmlNode, 'event-listener');
// if (event_listener) {
// var field = this._getChildrenNode(event_listener, "field");
// if (field) {
// var valueNode = this._getChildrenNode(field, "string");
// if (valueNode) {
// // stringValue格式 需为 "operation:审核;argxx:hello;argxxx:bye"类似的格式
// // 即参数间用;分开，参数key和value用：分开
// var stringValue = this._getAttribute(valueNode, 'value');
//
// var args = this._stringToMapObj(stringValue);
//
// var otherArg = new Array();
// for ( var key in args) {
// if (args[key] instanceof Function)
// continue;
// // 取操作参数
// if (key == 'operation') {
// tran.event = args['operation'];
// } else {
// // 其它参数
// otherArg.push(key + ":" + args[key]);
// }
// }
// tran.args = otherArg.join(";");
// }
// }
// }
// };
/**
 * 取得 xml node的子节点
 * 
 * @param xmlNode
 * @param tagName
 * @returns
 */
WorkflowDesigner.prototype._getChildrenNode = function(xmlNode, tagName) {
	var children = xmlNode.children;
	for ( var i in children) {
		if (children[i] instanceof Function)
			continue;
		if (children[i].tagName == tagName) {
			return children[i];
		}
	}
	return null;
};
/**
 * 取得节点的属性
 * 
 * @param xmlNode
 * @param attrName
 * @returns
 */
WorkflowDesigner.prototype._getAttribute = function(xmlNode, attrName) {
	var attrs = xmlNode.attributes;
	for ( var i in attrs) {
		if (attrs[i] instanceof Function)
			continue;

		if (attrs[i].name == attrName) {
			return attrs[i].value;
		}
	}
	return null;
};
/**
 * 字符串转对象
 * 
 * @param str
 * @returns
 */
WorkflowDesigner.prototype._stringToMapObj = function(str) {
	var obj = {};
	var array = str.split(";");
	for ( var i = 0; i < array.length; i++) {
		var word = array[i];
		if (word) {
			var pair = word.split(":");
			if (pair.length >= 2) {
				obj[pair[0]] = pair[1];
			}
		}
	}
	return obj;
};

/**
 * 将工作流节点（迁移线）的扩展属性填入到节点模型中去
 */
WorkflowDesigner.prototype._fillExtAttrsToActiMap = function() {
	for ( var i in this.extAttrCache) {
		var attr = this.extAttrCache[i];
		if (attr instanceof Function) {
			continue;
		}
		switch (attr.category) {
		case 'TRANS_BDATA_STATUS':
			// 迁称线目的地的数据状态
			var acti = this.actiMap[attr.srcacti];
			if (acti) {
				for ( var j in acti.transition) {
					if (acti.transition[j] instanceof Function)
						continue;

					if (acti.transition[j].name == attr.transition) {
						acti.transition[j].donestatus = attr.attrvalue1;
					}
				}
			}
			break;
		case 'TRANS_EVENT':
			// 迁称线目的地的数据状态
			var acti = this.actiMap[attr.srcacti];
			if (acti) {
				for ( var j in acti.transition) {
					if (acti.transition[j] instanceof Function)
						continue;

					if (acti.transition[j].name == attr.transition) {
						acti.transition[j].event = attr.attrvalue1;
						acti.transition[j].args = attr.attrvalue2;
					}
				}
			}
			break;

		case 'TASK_FORM_EDITABLE':
			// 表单可编辑性

			var acti = this.actiMap[attr.srcacti];
			if (acti) {
				acti.formeditable = attr.attrvalue1;
			}
			break;

		case 'TASK_START_EVENT':
			// 表单可编辑性

			var acti = this.actiMap[attr.srcacti];
			if (acti) {
				acti.startEvent = attr.attrvalue1;
				acti.startArgs = attr.attrvalue2;
			}
			break;

		case 'TASK_END_EVENT':
			// 表单可编辑性

			var acti = this.actiMap[attr.srcacti];
			if (acti) {
				acti.endEvent = attr.attrvalue1;
				acti.endArgs = attr.attrvalue2;
			}
			break;
		case 'TASK_BACKABLE':
			// 表单可编辑性

			var acti = this.actiMap[attr.srcacti];
			if (acti) {
				acti.returnable = attr.attrvalue1;
				acti.withdrawable = attr.attrvalue2;
				acti.bkfirstnode=attr.attrvalue3;
			}
			break;
		case 'TASK_PAGE_URL':
			// 表单可编辑性

			var acti = this.actiMap[attr.srcacti];
			if (acti) {
				acti.pageUrl = attr.attrvalue1;
			}
			break;
		case 'TASK_ASSIGN':
			// 表单可编辑性

			var acti = this.actiMap[attr.srcacti];
			if (acti) {
				acti.assignmentHandler = attr.attrvalue1;
				acti.assignmentArgs = attr.attrvalue4;
			}
			break;
			
		case 'DECISION_HANDLER':
			// 表单可编辑性

			var acti = this.actiMap[attr.srcacti];
			if (acti) {
				acti.decisionHandlerType = attr.attrvalue1;
				acti.decisionHandlerParam = attr.attrvalue2;
			}
			break;
		default:
			break;
		}
	}
};

/**
 * 由模型生成Jpdl文件
 */
WorkflowDesigner.prototype.generateJpdl = function() {
	var xml = new xmlwriter();
	// xml头部编写
	xml.beginnode("process");

	xml.attrib("name", this.processInfo.name);
	xml.attrib("xmlns", "http://jbpm.org/4.4/jpdl");
	xml.attrib("key", this.processInfo.key);
	xml.attrib("version", this.processInfo.version);

	for ( var i in this.actiArray) {

		var acti = this.actiArray[i];
		if (acti instanceof Function)
			continue;
		xml.beginnode(acti.tagName); // start activity

		xml.attrib("g", acti.x + ',' + acti.y + ',' + acti.w + ',' + acti.h);
		xml.attrib("name", acti.name);

		if (acti.assignee) {
			xml.attrib("assignee", acti.assignee);
		}

		if ("task" == acti.tagName) {

			// 如果assignee有值，不再设置assignmentHandler

			// 添加assignment handler
			if (acti.assignmentHandler != undefined
					&& acti.assignmentHandler != null
					&& acti.assignmentHandler.trim().length > 0) {
				xml.beginnode("assignment-handler");
				xml.attrib("class",
						"com.jbf.workflow.common.WfAssignmentHandler");
				xml.endnode(); // end assignment-handler
			}

			// 添加startEvent事件

			this._makeActivityEvent('start', xml);
			// 添加endEvent事件
			this._makeActivityEvent('end', xml);

		} else if ("decision" == acti.tagName) {
			// 如果设置了expr则handler不生效
			xml.beginnode("handler");
			xml.attrib("class", this.default_decision_handler); //标准的分支处理器代理类
			xml.endnode(); // end handler
		}
		if (acti.description) {

			xml.beginnode("description");
			xml.writestring(acti.description);
			xml.endnode(); // end description
		}
		if (acti.transition != null && acti.transition.length > 0) {
			for ( var j in acti.transition) {
				if (acti.transition[j] instanceof Function)
					continue;
				xml.beginnode("transition");
				xml.attrib("name", acti.transition[j].name);
				xml.attrib("to", acti.transition[j].to);

				var tmp_array = new Array();
				for ( var k in acti.transition[j].path.points) {
					if (acti.transition[j].path.points[k] instanceof Function)
						continue;

					if (k >= 1
							&& k <= acti.transition[j].path.points.length - 2) {
						var p = acti.transition[j].path.points[k];
						tmp_array.push(p.x + "," + p.y);
					}
				}
				var g = tmp_array.join(";");

				var textPos = acti.transition[j].text.relativeX + ','
						+ acti.transition[j].text.relativeY;

				if (g == '') {
					g = textPos;
				} else {
					g += ':' + textPos;
				}
				xml.attrib("g", g);

				// if (acti.transition[j].expr) {
				// xml.beginnode("condition");
				// xml.attrib("expr", acti.transition[j].expr);
				// xml.endnode(); // end condition
				// }
				// 添加迁移线的事件支持
				// if (acti.transition[j].event) {
				this._makeTransitionEvent(xml);
				// }
				xml.endnode(); // end transition
			}
		}

		xml.endnode(); // end activity
	}

	xml.endnode();
	var header = '<?xml version="1.0" encoding="UTF-8"?>';
	this.jpdl = header + xml.tostring();
	// document.getElementById("jpdl").value = header + xml.tostring();
};
/**
 * 生成活动节点的on start 和on end事件的jpdl定义
 * 
 * @param type
 *            类型 start 或 end
 * @param eventOperationName
 *            从配置中选择的操作类型名称
 * @param xmlWriter
 * 
 */
WorkflowDesigner.prototype._makeActivityEvent = function(type, xmlWriter) {
	xmlWriter.beginnode("on");
	xmlWriter.attrib("event", type);
	xmlWriter.beginnode("event-listener");

	if (type == 'start') {
		xmlWriter.attrib("class", this.default_startevent_listener);
	} else {
		xmlWriter.attrib("class", this.default_endevent_listener);
	}

	// xmlWriter.beginnode("field");
	// xmlWriter.attrib("name", "args");
	// xmlWriter.beginnode("string");
	//
	// var argValue = "operation:" + eventOperationName;
	// if (otherArgs != undefined && otherArgs != null
	// && otherArgs.trim().length > 0) {
	// argValue += ";" + otherArgs;
	// }
	// xmlWriter.attrib("value", argValue);
	// xmlWriter.endnode(); // end string
	// xmlWriter.endnode(); // end field
	xmlWriter.endnode(); // end event-listener
	xmlWriter.endnode(); // end on
};
/**
 * 生成迁移线的的事件的jpdl定义
 * 
 * @param eventOperationName
 *            从配置中选择的操作类型名称
 * @param xmlWriter
 */
WorkflowDesigner.prototype._makeTransitionEvent = function(xmlWriter) {
	xmlWriter.beginnode("event-listener");
	xmlWriter.attrib("class", this.default_trans_event_listener);
	// xmlWriter.beginnode("field");
	// xmlWriter.attrib("name", "args");
	// xmlWriter.beginnode("string");
	//
	// var argValue = "operation:" + eventOperationName;
	// if (otherArgs != undefined && otherArgs != null
	// && otherArgs.trim().length > 0) {
	// argValue += ";" + otherArgs;
	// }
	//
	// xmlWriter.attrib("value", argValue);
	// xmlWriter.endnode(); // end string
	// xmlWriter.endnode(); // end field
	xmlWriter.endnode(); // end event-listener
};

// 取得jpdl
WorkflowDesigner.prototype.getJpdl = function() {
	return this.jpdl;
};