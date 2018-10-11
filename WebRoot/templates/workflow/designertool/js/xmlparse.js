/**
 * 由Jpdl文件解析并构建模型
 */
function parseJpdl() {

	var xmlDoc = null;
	var text = document.getElementById("jpdl").value;

	var domParser = new DOMParser();
	xmlDoc = domParser.parseFromString(text, 'text/xml');
	var $agnt=navigator.userAgent.toLowerCase();
	var process = null;
		if($agnt.indexOf("chrome")>0 ||$agnt.indexOf("webkit")>0 )
		process = xmlDoc.childNodes[0];
	else
		process = xmlDoc.children[0];
	var proc_attr = process.attributes;
	processInfo = {};
	for ( var k in proc_attr) {
		var attr = proc_attr[k];
		if (attr instanceof Function)
			continue;
		
		processInfo[attr.name] = attr.value;
	}
	processInfo.description = undefined;

	showProcessProp();
	var nodes = process.children;
	actiArray = new Array();
	for ( var i in nodes) {
		node = nodes[i];
		if (node instanceof Function)
			continue;
		
		dealWith(node);
	}
	// 建立各shape的中心坐标
	for ( var i in actiArray) {
		var shape = actiArray[i];
		if (shape instanceof Function)
			continue;
		var locs = shape.g.split(",");

		actiArray[i].x = +locs[0];
		actiArray[i].y = +locs[1];
		actiArray[i].w = +locs[2];
		actiArray[i].h = +locs[3];

		actiArray[i].centerX = +locs[0] + (+locs[2]) / 2;
		actiArray[i].centerY = +locs[1] + (+locs[3]) / 2;
	}

	/***************************************************************************
	 * 建立各线的路径 对transition建立如下结构 transition.path={ orgX, orgY, destX, destY,
	 * points:[ {x0,y0}, {x1,y1}, ... ] }
	 **************************************************************************/
	actiMap = new Array();
	for ( var i in actiArray) {
		var tempNd = actiArray[i];
		if (tempNd instanceof Function)
			continue;
		
		if (tempNd.name) {
			actiMap[tempNd.name] = tempNd;
		}
	}

	for ( var i in actiArray) {
		var node = actiArray[i];
		if (node instanceof Function)
			continue;
		for ( var j in node.transition) {
			var tran = node.transition[j];
			if (tran instanceof Function)
				continue;
			buildTransitionStruct(tran, node);
		}
	}

	canvas.onmousedown = mouseDown;
	canvas.onmouseup = mouseUp;
	canvas.onmousemove = mouseMove;
	window.addEventListener('keydown', designer_canvas_keyDown, true);

	repaintCanvas();
}

function dealWith(node) {
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
			dealTransitionEvent(trans,children[i]);
			nd.transition.push(trans);
			break;
		case 'handler':
			if (nd.tagName == 'decision') {
				nd.handler = children[i].attributes[0].value;
			}
			break;

		case 'assignment-handler':
			if (nd.tagName == 'task') {
				nd.assignmentHandler = children[i].attributes[0].value;
			}
			break;
		case 'on':
			dealOnEvent(nd,children[i]);
			break;
		case 'description':
			nd.description=children[i].textContent;
			break;
		}
	}
	actiArray.push(nd);
}


function dealOnEvent(activity,xmlNode){
	// 事务名
	var event=null;
	var attrs=xmlNode.attributes;
	for(var i in attrs){
		if (attrs[i] instanceof Function)
			continue;
		if(attrs[i].name=='event'){
			event=attrs[i].value;
			break;
		}
	}
	var event_listener=getChildren(xmlNode,'event-listener');
	if(event_listener){
		var field=getChildren(event_listener,"field");
		if(field){
			var valueNode=getChildren(field,"string");
			if(valueNode){
				var stringValue=getAttribute(valueNode,'value');
				var args=stringToMapObj(stringValue);
				
				var otherArg=new Array();
				for(var key in args){
					if (args[key] instanceof Function)
						continue;
					// 取操作参数
					if(key=='operation'){
						if(args['operation']=='null'){
							activity[event+'Event']=null;
						}else{
							activity[event+'Event']=args['operation'];
						}
					}else{
						// 其它参数
						otherArg.push(key+":"+args[key]);
					}
				}
				activity[event+'Args']=otherArg.join(";");
			
			}
		}
		
	}
}

function dealTransitionEvent(tran,xmlNode){

	var event_listener=getChildren(xmlNode,'event-listener');
	if(event_listener){
		var field=getChildren(event_listener,"field");
		if(field){
			var valueNode=getChildren(field,"string");
			if(valueNode){
				// stringValue格式 需为 "operation:审核;argxx:hello;argxxx:bye"类似的格式
				// 即参数间用;分开，参数key和value用：分开
				var stringValue=getAttribute(valueNode,'value');
				
				var args=stringToMapObj(stringValue);
			   
				var otherArg=new Array();
				for(var key in args){
					if (args[key] instanceof Function)
						continue;
					// 取操作参数
					if(key=='operation'){
						tran.event=args['operation'];
					}else{
						// 其它参数
						otherArg.push(key+":"+args[key]);
					}
				}
				tran.args=otherArg.join(";");
			}
		}
	}
	
	
}

function getChildren(xmlNode,tagName){
	var children=xmlNode.children;
	for(var i in children){
		if (children[i] instanceof Function)
			continue;
		if(children[i].tagName==tagName){
			return children[i];
		}
	}
	return null;
}

function getAttribute(xmlNode,attrName){
	var attrs=xmlNode.attributes;
	for(var i in attrs){
		if (attrs[i] instanceof Function)
			continue;
		
		if(attrs[i].name==attrName){
			return attrs[i].value;
		}
	}
	return null;
}

function stringToMapObj(str){
	var obj={};
	var array=str.split(";");
	for(var i=0 ;i <array.length;i++){
		var word=array[i];
		if(word){
			var pair=word.split(":");
			if(pair.length>=2){
				obj[pair[0]]=pair[1];
			}
		}
	}
	return obj;
}

/**
 * 由模型生成Jpdl文件
 */
function generateJpdl() {
	var xml = new xmlwriter();
	// xml头部编写
	xml.beginnode("process");

	xml.attrib("name", processInfo.name);
	xml.attrib("xmlns", "http://jbpm.org/4.4/jpdl");
	xml.attrib("key", processInfo.key);
	xml.attrib("version", processInfo.version);

	for ( var i in actiArray) {

		var acti = actiArray[i];
		if (acti instanceof Function)
			continue;
		xml.beginnode(acti.tagName); // start activity

		xml.attrib("g", acti.x + ',' + acti.y + ',' + acti.w + ',' + acti.h);
		xml.attrib("name", acti.name);

		if (acti.assignee) {
			xml.attrib("assignee", acti.assignee);
		}

		if ("task" == acti.tagName) {
			if (!acti.assignee) {
				// 如果assignee有值，不再设置assignmentHandler

				// 添加assignment handler
				if (acti.assignmentHandler != undefined
						&& acti.assignmentHandler != null
						&& acti.assignmentHandler.trim().length > 0) {
					xml.beginnode("assignment-handler");
					xml.attrib("class", acti.assignmentHandler);
					xml.endnode(); // end assignment-handler
				}
			}
			// 添加startEvent事件
			if (acti.startEvent != undefined && acti.startEvent != null
					&& acti.startEvent.trim().length > 0) {
				makeActivityEvent('start', acti.startEvent,acti.startArgs, xml);
			}else{
				makeActivityEvent('start', null,null, xml);
			}
			// 添加endEvent事件
			if (acti.endEvent != undefined && acti.endEvent != null
					&& acti.endEvent.trim().length > 0) {
				makeActivityEvent('end', acti.endEvent,acti.endArgs, xml);
			}

		} else if ("decision" == acti.tagName) {
			// 如果设置了expr则handler不生效
			if (acti.expr) {
				xml.attrib("expr", acti.expr);
			} else if (acti.handler) {
				xml.beginnode("handler");
				xml.attrib("class", acti.handler);
				xml.endnode(); // end handler
			}
		}
		if(acti.description){
			
			xml.beginnode("description");
			xml.writestring(acti.description);
			xml.endnode(); //end description
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

				if (acti.transition[j].expr) {
					xml.beginnode("condition");
					xml.attrib("expr", acti.transition[j].expr);
					xml.endnode(); // end condition
				}
				// 添加迁移线的事件支持
				if (acti.transition[j].event) {
					makeTransitionEvent(acti.transition[j].event,acti.transition[j].args, xml);
				}
				xml.endnode(); // end transition
			}
		}
		
		
		xml.endnode(); // end activity
	}

	xml.endnode();
	var header = '<?xml version="1.0" encoding="UTF-8"?>';
	document.getElementById("jpdl").value = header + xml.tostring();
}
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
function makeActivityEvent(type, eventOperationName,otherArgs,xmlWriter) {
	xmlWriter.beginnode("on");
	xmlWriter.attrib("event", type);
	xmlWriter.beginnode("event-listener");
	
	if(type=='start'){
		xmlWriter.attrib("class", default_startevent_listener);
	}else{
		xmlWriter.attrib("class", default_event_listener);
	}
	
	xmlWriter.beginnode("field");
	xmlWriter.attrib("name", "args");
	xmlWriter.beginnode("string");
	
	var argValue= "operation:" + eventOperationName;
	if(otherArgs!=undefined && otherArgs!=null && otherArgs.trim().length>0){
		argValue+=";"+otherArgs;
	}
	xmlWriter.attrib("value",argValue);
	xmlWriter.endnode(); // end string
	xmlWriter.endnode(); // end field
	xmlWriter.endnode(); // end event-listener
	xmlWriter.endnode(); // end on
}
/**
 * 生成迁移线的的事件的jpdl定义
 * 
 * @param eventOperationName
 *            从配置中选择的操作类型名称
 * @param xmlWriter
 */
function makeTransitionEvent(eventOperationName, otherArgs,xmlWriter) {
	xmlWriter.beginnode("event-listener");
	xmlWriter.attrib("class", default_event_listener);
	xmlWriter.beginnode("field");
	xmlWriter.attrib("name", "args");
	xmlWriter.beginnode("string");
	
	var argValue= "operation:" + eventOperationName;
	if(otherArgs!=undefined && otherArgs!=null && otherArgs.trim().length>0){
		argValue+=";"+otherArgs;
	}
	
	xmlWriter.attrib("value",argValue);
	xmlWriter.endnode(); // end string
	xmlWriter.endnode(); // end field
	xmlWriter.endnode(); // end event-listener
}
function viewJpdl() {
	generateJpdl();
	$('#xml_window').window('open');
}

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
