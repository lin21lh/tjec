/**
 * 模型更新
 * 
 * @param k
 */
WorkflowDesigner.prototype._updateModel = function(k) {

	// ...
};

/*******************************************************************************
 * 事件处理
 * 
 ******************************************************************************/
WorkflowDesigner.prototype.dealMouseDown = function(e) {
	console.log('mouse down');
	this.mouseDownLoc.x = e.offsetX;
	this.mouseDownLoc.y = e.offsetY;
	this.mousePressed = true;
	switch (this.opMode) {
	case 'select':
		for ( var i in this.actiArray) {
			var node = this.actiArray[i];
			if (node instanceof Function)
				continue;
			// 判断是否有物体被选中
			if (e.offsetX >= node.x && e.offsetY >= node.y
					&& e.offsetX <= (node.x + node.w)
					&& e.offsetY <= (node.y + node.h)) {
				if (node.name != this.activeObjectName) {

					console.log('Gained Foucs : ' + node.name);
					this.activeObjectName = node.name;
					this.activeLine = null;
					this._showProperties(this.activeObjectName);
				}
				this._repaintCanvas();
				return;
			}
		}

		for ( var i in this.actiArray) {
			if (this.actiArray[i] instanceof Function)
				continue;
			var trans = this.actiArray[i].transition;

			for ( var j in trans) {
				var tran = trans[j];
				if (tran instanceof Function)
					continue;
				for ( var k = 0; k < tran.path.points.length - 1; k++) {
					if (this._inRectangle(e.offsetX, e.offsetY,
							tran.path.points[k].x, tran.path.points[k].y,
							tran.path.points[k + 1].x,
							tran.path.points[k + 1].y)) {

						this.activeLine = {};
						this.activeLine.name = tran.name;
						this.activeLine.transition = tran;
						this.activeLine.from = this.actiArray[i];
						this.activeLine.to = tran.to;

						this._calcActiveLineEditingPoints();
						continueFlag = false;
						this.activeObjectName = null;
						this._showTransitionProp(this.activeLine);
						this._repaintCanvas();
						return;
					}
				}
			}
		}
		this.activeObjectName = null;
		this.activeLine = null;

		this._showProcessProp();
		break;
	case 'transitionCreate':
		this.transitionDraw.src.name = null;
		for ( var i in this.actiArray) {
			var node = this.actiArray[i];
			if (node instanceof Function)
				continue;
			if (e.offsetX >= node.x && e.offsetY >= node.y
					&& e.offsetX <= (node.x + node.w)
					&& e.offsetY <= (node.y + node.h)) {
				this.transitionDraw.src.name = node.name;
				this.transitionDraw.src.x = node.centerX;
				this.transitionDraw.src.y = node.centerY;
				break;
			}
		}

		break;
	case 'activityCreate':
		if (this.editMode == 'NEW') {
			this.activeObjectName = this._createActivity(this.toolbox.widget,
					e.offsetX, e.offsetY);
			this._showProperties(this.activeObjectName);
		}
		break;
	}
	this._repaintCanvas();

};

WorkflowDesigner.prototype.dealMouseUp = function(e) {
	console.log('mouse up');
	this.mousePressed = false;
	this.virtualPoint.visible = false;
	switch (this.opMode) {
	case 'select':

		this.drawVirtualBox.visible = false;
		var movementX = e.offsetX - this.mouseDownLoc.x;
		var movementY = e.offsetY - this.mouseDownLoc.y;

		if (movementX != 0 || movementY != 0) {

			if (this.editMode == 'NEW') {
				if (this.activeObjectName) {
					this._moveActivity(this.activeObjectName, movementX,
							movementY);
					this._showProperties(this.activeObjectName);
					this._repaintCanvas();
				} else if (this.activeLine) {
					if (this.editingPointToMove.type != null) {
						this.editingPointToMove.destX = e.offsetX;
						this.editingPointToMove.destY = e.offsetY;
						this._updateTransition();
						this._repaintCanvas();
						this._showTransitionProp(this.activeLine);
					}
				}
			} else {
				this._repaintCanvas();
			}
		}

		break;
	case 'transitionCreate':
		console.log("transitionCreate - mouse up");
		if (this.editMode == 'NEW') {
			this.transitionDraw.dest.name = null;
			for ( var i in this.actiArray) {
				var node = this.actiArray[i];
				if (node instanceof Function)
					continue;

				if (e.offsetX >= node.x && e.offsetY >= node.y
						&& e.offsetX <= (node.x + node.w)
						&& e.offsetY <= (node.y + node.h)) {
					this.transitionDraw.dest.name = node.name;
					this.transitionDraw.dest.x = node.centerX;
					this.transitionDraw.dest.y = node.centerY;
					this.transitionDraw.dest.description = node.description;
					break;
				}
			}
			console.log("transitionCreate - mouse up 2");
			// 起始和目标都有组件
			if (this.transitionDraw.src.name != null
					&& this.transitionDraw.dest.name != null
					&& this.transitionDraw.src.name != this.transitionDraw.dest.name) {
				this._addTransition(this.transitionDraw);
			}
		}
		this._repaintCanvas();
		break;
	case 'activityCreate':
		$('#widgets').datagrid('selectRow', 0);
		this.opMode = 'select';
		break;
	}
};

WorkflowDesigner.prototype.dealMouseMove = function(e) {

	switch (this.opMode) {
	case 'select':
		if (this.mousePressed) {
			if (this.activeObjectName) {
				var movementX = e.offsetX - this.mouseDownLoc.x;
				var movementY = e.offsetY - this.mouseDownLoc.y;

				var shape = this.actiMap[this.activeObjectName];

				this.drawVirtualBox.visible = true;
				this.drawVirtualBox.x = shape.x + movementX;
				this.drawVirtualBox.y = shape.y + movementY;
				this.drawVirtualBox.w = shape.w;
				this.drawVirtualBox.h = shape.h;
				this._repaintCanvas();
			} else if (this.activeLine) {
				this.virtualPoint.visible = true;
				this.virtualPoint.x = e.offsetX;
				this.virtualPoint.y = e.offsetY;
				this._repaintCanvas();
			}
		} else if (this.activeLine) {
			for ( var i in this.activeLine.editingPoints.orgPoints) {
				var p = this.activeLine.editingPoints.orgPoints[i];
				if (p instanceof Function)
					continue;
				if ((p.x - e.offsetX) * (p.x - e.offsetX) + (p.y - e.offsetY)
						* (p.y - e.offsetY) <= 16) {
					this._getContext().canvas.style.cursor = 'crosshair';

					this.editingPointToMove.type = 'orgPoint';
					this.editingPointToMove.index = i;
					return;
				}
			}

			for ( var i in this.activeLine.editingPoints.virtualPoints) {
				var p = this.activeLine.editingPoints.virtualPoints[i];
				if (p instanceof Function)
					continue;
				if ((p.x - e.offsetX) * (p.x - e.offsetX) + (p.y - e.offsetY)
						* (p.y - e.offsetY) <= 16) {
					this._getContext().canvas.style.cursor = 'crosshair';
					this.editingPointToMove.type = 'virtualPoint';
					this.editingPointToMove.index = i;
					return;
				}
			}
			this.editingPointToMove.type = null;
			this.editingPointToMove.index = -1;
			this._getContext().canvas.style.cursor = 'default';

		}
		break;
	case 'transitionCreate':

		break;
	case 'activityCreate':
		break;
	}
};

WorkflowDesigner.prototype.dealDesignerCanvasKeyDown = function(e) {
	switch (this.opMode) {
	case 'select':
		var keyCode = e.keyCode ? e.keyCode : e.which;
		if (keyCode == 46) {
			if (this.activeObjectName != null && this.propEditing == false) {
				console.log("deleting activity " + this.activeObjectName);
				this._removeActivity(this.activeObjectName);
			} else if (this.activeLine != null && this.propEditing == false) {

				if (this.editingPointToMove.type == null) {
					// 删除整条线
					console.log("deleting transition " + this.activeLine.name
							+ " from " + this.activeLine.from);
					this._removeTransition(this.activeLine);
					this.activeLine = null;
				} else if (this.editingPointToMove.type == 'orgPoint') {
					// 移除控制点
					var acti = this.activeLine.from;

					for ( var i in acti.transition) {
						if (acti.transition[i] instanceof Function)
							continue;
						if (acti.transition[i].name == this.activeLine.name) {
							var points = acti.transition[i].path.points;
							points.splice(+this.editingPointToMove.index, 1);
							this._calcActiveLineEditingPoints();
							this._buildTextLoc(acti.transition[i]);
						}
					}
				}
			}
			this._repaintCanvas();
		}

		break;
	}

};

/*******************************************************************************
 * 操作逻辑部分
 * 
 ******************************************************************************/
// 移动Activity节点
WorkflowDesigner.prototype._moveActivity = function(actiName, movX, movY) {
	var curActi = this.actiMap[actiName];
	curActi.x += movX;
	curActi.y += movY;

	curActi.centerX += movX;
	curActi.centerY += movY;
	this._updateActivityTransitions(curActi);
};

WorkflowDesigner.prototype._updateActivityTransitions = function(acti) {
	// 2移动shape相关的transition
	// 2.1 outcome transition
	for ( var i in acti.transition) {
		var tran = acti.transition[i];
		if (tran instanceof Function)
			continue;

		tran.path.orgX = acti.centerX;
		tran.path.orgY = acti.centerY;

		tran.path.points[0].x = tran.path.orgX;
		tran.path.points[0].y = tran.path.orgY;

		if (tran.path.points.length == 2) {
			this._buildTextLoc(tran);
		}
	}
	// 2.2 income transition

	for ( var i in this.actiArray) {
		var node = this.actiArray[i];
		if (node instanceof Function)
			continue;

		if (node.name == acti.name) {
			continue;
		}

		for ( var j in node.transition) {
			if (node.transition[j] instanceof Function)
				continue;

			if (acti.name == node.transition[j].to) {
				var tran = node.transition[j];
				tran.path.destX = acti.centerX;
				tran.path.destY = acti.centerY;

				tran.path.points[tran.path.points.length - 1].x = tran.path.destX;
				tran.path.points[tran.path.points.length - 1].y = tran.path.destY;
				this._buildTextLoc(tran);
			}
		}

	}
};

WorkflowDesigner.prototype._createActivity = function(widgetName, x, y) {
	var newName = this._getNextActivityName(widgetName);
	var rec_w, rec_h;
	switch (widgetName) {

	case 'start':
	case 'end':
	case 'decision':
	case 'rule':
	case 'fork':
	case 'join':
		rec_w = 48;
		rec_h = 48;
		break;
	default:
		rec_w = 92;
		rec_h = 52;
		break;
	}
	var newActi = {
		x : x,
		y : y,
		w : rec_w,
		h : rec_h,
		centerX : x + rec_w / 2,
		centerY : y + rec_h / 2,
		tagName : widgetName,
		name : newName,
		description : newName
	};
	switch (widgetName) {
	case 'task':
		newActi.assignmentHandler = "角色分配器";
		newActi.assignmentArgs = "";

		newActi.startEvent = "";
		newActi.startArgs = "";

		newActi.endEvent = "";
		newActi.endArgs = "";

		newActi.returnable = "false";
		newActi.withdrawable = "false";
		newActi.bkfirstnode = "false";

		newActi.pageUrl = "";
		newActi.formeditable = "false";

		// 创建扩展属性缓存
		// 1.表单编辑属性

		this.extAttrCache.push({
			id : null,
			key : this.processInfo.key,
			version : this.processInfo.version,
			category : 'TASK_ASSIGN',
			srcacti : newName,
			transition : '',
			tgtacti : '',
			attrvalue1 : '角色分配器',
			attrvalue4 : ''
		});
		this.extAttrCache.push({
			id : null,
			key : this.processInfo.key,
			version : this.processInfo.version,
			category : 'TASK_START_EVENT',
			srcacti : newName,
			transition : '',
			tgtacti : '',
			attrvalue1 : '',
			attrvalue2 : ''
		});

		this.extAttrCache.push({
			id : null,
			key : this.processInfo.key,
			version : this.processInfo.version,
			category : 'TASK_END_EVENT',
			srcacti : newName,
			transition : '',
			tgtacti : '',
			attrvalue1 : '',
			attrvalue2 : ''
		});

		this.extAttrCache.push({
			id : null,
			key : this.processInfo.key,
			version : this.processInfo.version,
			category : 'TASK_BACKABLE',
			srcacti : newName,
			transition : '',
			tgtacti : '',
			attrvalue1 : 'false',
			attrvalue2 : 'false'
		});

		this.extAttrCache.push({
			id : null,
			key : this.processInfo.key,
			version : this.processInfo.version,
			category : 'TASK_PAGE_URL',
			srcacti : newName,
			transition : '',
			tgtacti : '',
			attrvalue1 : ''
		});

		this.extAttrCache.push({
			id : null,
			key : this.processInfo.key,
			version : this.processInfo.version,
			category : 'TASK_FORM_EDITABLE',
			srcacti : newName,
			transition : '',
			tgtacti : '',
			attrvalue1 : 'false'
		});

		break;

	case 'decision':
		newActi.decisionHandlerType = "自定义";
		newActi.decisionHandlerParam = "";
		this.extAttrCache.push({
			id : null,
			key : this.processInfo.key,
			version : this.processInfo.version,
			category : 'DECISION_HANDLER',
			srcacti : newName,
			transition : '',
			tgtacti : '',
			attrvalue1 : '自定义',
			attrvalue2 : ''
		});
		break;

	}

	// 创建元件缓存
	this.actiArray.push(newActi);
	this.actiMap[newName] = newActi;
	return newName;
};

WorkflowDesigner.prototype._getNextActivityName = function(tagName) {
	var nextNum = 1;
	for ( var i in this.actiArray) {
		if (this.actiArray[i] instanceof Function)
			continue;
		if (this.actiArray[i].name.indexOf(tagName) == 0) {
			var otherName = this.actiArray[i].name.substring(tagName.length);

			try {
				var num = parseInt(otherName);
				if (num >= nextNum) {
					nextNum = num + 1;
				}
			} catch (e) {

			}
		}
	}
	return tagName + nextNum;
};
// 迁移线位置
WorkflowDesigner.prototype._buildTextLoc = function(tran) {
	var points = tran.path.points;
	var len = points.length;

	if (len % 2 == 1) {
		var index = Math.floor(len / 2);

		tran.text.centerX = points[index].x;
		tran.text.centerY = points[index].y;
	} else {
		var index = len / 2;
		tran.text.centerX = (points[index].x + points[index - 1].x) / 2;
		tran.text.centerY = (points[index].y + points[index - 1].y) / 2;
	}
	tran.text.x = tran.text.centerX + tran.text.relativeX;
	tran.text.y = tran.text.centerY + tran.text.relativeY;

};

WorkflowDesigner.prototype._getTransitionArrow = function(transition) {
	var destShape = this.actiMap[transition.to];

	// 取得最后一条线的向量

	var points = transition.path.points;

	var len = points.length;
	var lastPoint = points[len - 1];
	var prevPoint = points[len - 2];

	var vector = {
		x : prevPoint.x - lastPoint.x,
		y : prevPoint.y - lastPoint.y
	};

	var angle = 0;

	// 竖直的
	if (vector.x == 0) {

		if (vector.y < 0) {
			return {
				x : 0,
				y : destShape.h / (-2),
				angle : 3 * Math.PI / 2
			};
		} else {
			return {
				x : 0,
				y : destShape.h / (2),
				angle : Math.PI / 2
			};
		}
	} else {

		var result = {};
		angle = Math.atan(vector.y / vector.x);

		var ref_angle = Math.atan(destShape.h / destShape.w);

		if (angle > ref_angle) {
			result.y = destShape.h / 2;
			result.x = destShape.h / 2 / Math.tan(angle);
			result.angle = angle;
		} else if (angle > 0) {
			result.y = destShape.w / 2 * Math.tan(angle);
			result.x = destShape.w / 2;
			result.angle = angle;
		} else if (angle > (-1) * ref_angle) {
			result.y = destShape.w / 2 * Math.tan(angle);
			result.x = destShape.w / 2;
			result.angle = angle;
		} else {
			result.y = -destShape.h / 2;
			result.x = -destShape.h / 2 / Math.tan(angle);
			result.angle = angle;
		}

		if (vector.x < 0) {
			result.x = -result.x;
			result.y = -result.y;
			result.angle += Math.PI;
		}

		return result;
	}

};
/**
 * 新增迁移线
 * 
 * @param transDraw
 */
WorkflowDesigner.prototype._addTransition = function(transDraw) {

	console.log('Adding Transistion!');
	var tran = {
		name : '发送至' + transDraw.dest.description,
		to : transDraw.dest.name,
		g : '-76,-12',
		event : '',
		args : '',
		donestatus : ''
	};
	var oldTrans = this.actiMap[transDraw.src.name].transition;
	for ( var i in oldTrans) {
		if (oldTrans[i] instanceof Function)
			continue;

		if (oldTrans[i].to == tran.to) {
			console.log("路径已存在，不能重复添加！");
			return;
		}
	}

	this._buildTransitionStruct(tran, this.actiMap[transDraw.src.name]);
	if (undefined == this.actiMap[transDraw.src.name].transition) {
		this.actiMap[transDraw.src.name].transition = new Array();
	}
	this.actiMap[transDraw.src.name].transition.push(tran);

	// 创建扩展属性缓存
	// 过后状态属性

	this.extAttrCache.push({
		id : null,
		key : this.processInfo.key,
		version : this.processInfo.version,
		category : 'TRANS_BDATA_STATUS',
		srcacti : transDraw.src.name,
		transition : tran.name,
		tgtacti : '',
		attrvalue1 : ''
	});

	this.extAttrCache.push({
		id : null,
		key : this.processInfo.key,
		version : this.processInfo.version,
		category : 'TRANS_EVENT',
		srcacti : transDraw.src.name,
		transition : tran.name,
		tgtacti : '',
		attrvalue1 : '',
		attrvalue2 : ''
	});

};

/**
 * 建立迁移线的结构
 * 
 * @param tran
 */
WorkflowDesigner.prototype._buildTransitionStruct = function(tran, srcActi) {

	if (tran instanceof Function)
		return;

	tran.path = {};
	tran.path.orgX = srcActi.centerX;
	tran.path.orgY = srcActi.centerY;

	var dest = this.actiMap[tran.to];

	tran.path.destX = dest.centerX;
	tran.path.destY = dest.centerY;

	if (tran.g) {
		var pathNodeStr = tran.g.split(':');
		var textLocStr = pathNodeStr[pathNodeStr.length - 1];
		tran.text = {};
		tran.text.relativeX = +(textLocStr.split(',')[0]);
		tran.text.relativeY = +(textLocStr.split(',')[1]);
		tran.path.points = new Array();
		tran.path.points[0] = {
			x : tran.path.orgX,
			y : tran.path.orgY
		};
		var pointListLength = 0;

		if (pathNodeStr.length > 1) {
			var pointList = pathNodeStr[0].split(";");
			pointListLength = pointList.length;
			for ( var m = 0; m < pointList.length; m++) {
				var loc = pointList[m].split(',');
				tran.path.points[m + 1] = {
					x : +loc[0],
					y : +loc[1]
				};
			}
		}
		tran.path.points[pointListLength + 1] = {
			x : tran.path.destX,
			y : tran.path.destY
		};

		this._buildTextLoc(tran);

	}
};

WorkflowDesigner.prototype._removeTransition = function(activeLineArg) {

	var acti = activeLineArg.from;
	if (acti) {
		var trans = acti.transition;
		for ( var i = 0; i < trans.length; i++) {
			if (trans[i].to == activeLineArg.to) {
				trans.splice(i, 1);
			}
		}
	}

};

WorkflowDesigner.prototype._removeActivity = function(actiName) {
	// 先删除到达activity的迁移线

	for ( var i in this.actiArray) {
		var activityNode = this.actiArray[i];
		for ( var j in activityNode.transition) {
			if (activityNode.transition[j] instanceof Function)
				continue;

			if (activityNode.transition[j].to == actiName) {
				activityNode.transition.splice(j, 1);
				break;
			}
		}
	}
	// 删除activity
	this.actiMap[actiName] = undefined;
	for ( var i in this.actiArray) {
		if (this.actiArray[i] instanceof Function)
			continue;

		if (this.actiArray[i].name == actiName) {
			this.actiArray.splice(i, 1);
		}
	}
};

WorkflowDesigner.prototype._changeProperties = function() {
	// var rows = $(this.propGirdId).propertygrid('getRows');
	var rows = $(this.propGirdId).propertygrid('getChanges', 'updated');
	if (null != this.activeObjectName && "" != this.activeObjectName) {
		console.log("====== on change ============= " + this.activeObjectName);

		if (this.editMode == 'UPDATE') {
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i].field == 'name' || rows[i].field == 'description') {
					this.warn("流程图发布以后，节点的ID、标签属性不可再修改！");
					$(this.propGirdId).propertygrid('rejectChanges');
					// 执行中止！！！
					return;
				}
			}
		}

		var acti = this.actiMap[this.activeObjectName];
		if (acti) {
			for ( var i = 0; i < rows.length; i++) {
				switch (rows[i].field) {
				case 'x':
				case 'y':
				case 'w':
				case 'h':
					acti[rows[i].field] = +rows[i].value;
					break;
				case 'name':
					var newName = rows[i].value;

					if (newName != this.oldActiName) {
						// 更新名称
						this._changeActiName(newName, acti);
					}
					acti[rows[i].field] = newName;

					break;

				case 'assignmentHandler':

					acti[rows[i].field] = rows[i].value;

					this._updateExtAttrProperty("TASK_ASSIGN", acti.name, "",
							"attrvalue1", rows[i].value);
					break;
				case 'assignmentArgs':
					acti[rows[i].field] = rows[i].value;

					this._updateExtAttrProperty("TASK_ASSIGN", acti.name, "",
							"attrvalue4", rows[i].value);
					break;

				case 'startEvent':
					acti[rows[i].field] = rows[i].value;

					this._updateExtAttrProperty("TASK_START_EVENT", acti.name,
							"", "attrvalue1", rows[i].value);
					break;

				case 'endEvent':
					acti[rows[i].field] = rows[i].value;

					this._updateExtAttrProperty("TASK_END_EVENT", acti.name,
							"", "attrvalue1", rows[i].value);
					break;

				case 'startArgs':
					acti[rows[i].field] = rows[i].value;

					this._updateExtAttrProperty("TASK_START_EVENT", acti.name,
							"", "attrvalue2", rows[i].value);
					break;

				case 'endArgs':
					acti[rows[i].field] = rows[i].value;

					this._updateExtAttrProperty("TASK_END_EVENT", acti.name,
							"", "attrvalue2", rows[i].value);
					break;

				case 'returnable':
					acti[rows[i].field] = rows[i].value;

					this._updateExtAttrProperty("TASK_BACKABLE", acti.name, "",
							"attrvalue1", rows[i].value);
					break;

				case 'withdrawable':
					acti[rows[i].field] = rows[i].value;

					this._updateExtAttrProperty("TASK_BACKABLE", acti.name, "",
							"attrvalue2", rows[i].value);
					break;
					
				case 'bkfirstnode':
					acti[rows[i].field] = rows[i].value;
					this._updateExtAttrProperty("TASK_BACKABLE", acti.name, "",
							"attrvalue3", rows[i].value);
					break;
				case 'pageUrl':
					acti[rows[i].field] = rows[i].value;

					this._updateExtAttrProperty("TASK_PAGE_URL", acti.name, "",
							"attrvalue1", rows[i].value);
					break;
				case 'formeditable':
					acti[rows[i].field] = rows[i].value;
					// 更新extAttrCache缓存
					this._updateExtAttrProperty("TASK_FORM_EDITABLE",
							acti.name, "", "attrvalue1", rows[i].value);
					break;

				case 'decisionHandlerType':
					acti[rows[i].field] = rows[i].value;
					// 更新extAttrCache缓存
					this._updateExtAttrProperty("DECISION_HANDLER", acti.name,
							"", "attrvalue1", rows[i].value);
					break;
				case 'decisionHandlerParam':
					acti[rows[i].field] = rows[i].value;
					// 更新extAttrCache缓存
					this._updateExtAttrProperty("DECISION_HANDLER", acti.name,
							"", "attrvalue2", rows[i].value);
					break;

				default:
					acti[rows[i].field] = rows[i].value;
					break;
				}

			}
			this._updateActi(acti);
		}
	} else if (this.activeLine != null && this.activeLine.name != null) {
		// var rows = $(this.propGirdId).propertygrid('getRows');
		var rows = $(this.propGirdId).propertygrid('getChanges', 'updated');

		// editMode为UPDATE 时需检查有没有名称修改，如果有，则不合法
		if (this.editMode == 'UPDATE') {
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i].field == 'name') {
					this.warn("流程图发布以后，迁称线的名称属性不可再修改！");
					$(this.propGirdId).propertygrid('rejectChanges');
					// 执行中止！！！
					return;
				}
			}
		}

		for ( var j = 0; j < rows.length; j++) {

			// 待测试
			switch (rows[j].field) {
			case 'event':
				this._updateExtAttrProperty("TRANS_EVENT",
						this.activeLine.from.name, this.activeLine.name,
						"attrvalue1", rows[j].value);
				break;
			case 'args':
				this._updateExtAttrProperty("TRANS_EVENT",
						this.activeLine.from.name, this.activeLine.name,
						"attrvalue2", rows[j].value);
				break;
			case 'donestatus':
				this._updateExtAttrProperty("TRANS_BDATA_STATUS",
						this.activeLine.from.name, this.activeLine.name,
						"attrvalue1", rows[j].value);
				break;

			case 'name':
				// 修改迁移线name,需要更新原来的
				var oldName = this.activeLine.name;
				this.activeLine.name = rows[j].value;
				this._updateTransitionName(this.activeLine.from.name, oldName,
						rows[j].value);
				break;
			}
		}
		var trans = this.activeLine.from.transition;
		for ( var i = 0; i < trans.length; i++) {

			if (trans[i].name == this.activeLine.name) {

				for ( var j = 0; j < rows.length; j++) {
					switch (rows[j].field) {
					// 已在_updateTransitionName中更新
					// case 'name':
					// trans[i][rows[j].field] = rows[j].value;
					// this.activeLine.name = rows[j].value;
					// break;
					case 'x':
						trans[i].text.relativeX = +rows[j].value;
						break;
					case 'y':
						trans[i].text.relativeY = +rows[j].value;
						break;
					case 'event':
					case 'args':
					case 'donestatus':
						trans[i][rows[j].field] = rows[j].value;
						break;
					}

					break;
				}

				this._buildTextLoc(trans[i]);
			}
		}
		;
	} else {
		// 修改流程信息
		for ( var i = 0; i < rows.length; i++) {

			if ("key" == rows[i].field || "version" == rows[i].field
					|| "name" == rows[i].field) {
				var h_ = this;
				this.warn('名称、标识、版本不能进行修改！');
				$(this.propGirdId).propertygrid('rejectChanges');
				return;

			}
			this.processInfo[rows[i].field] = rows[i].value;

		}
		if (this.editMode == 'UPDATE') {
			var p_handle = this.processInfo;
			// 提交启用日期、停用日期到后台保存
			$.post(urls.editWorkflowProcVersion, p_handle, function(msg) {
				easyui_auto_notice(msg, null, null, "操作时发生异常！");
			}, "JSON");
		}
	}
	this._repaintCanvas();
	// 如果新建模式不需要立即提交服务端
	if (this.editMode == "NEW") {
		$(this.propGirdId).propertygrid('acceptChanges', 'updated');
	} else {
		// 立即发送到服务端进行保存

	}
};

WorkflowDesigner.prototype._updateActi = function(acti) {
	acti.centerX = acti.x + acti.w / 2;
	acti.centerY = acti.y + acti.h / 2;

	this._updateActivityTransitions(acti);
};
// activeLine.editingPoints 结构说明
// activeLine.editingPoints = [ {
// orgPoints : { //实点，虚拟点
// x : 0,
// y : 0,
// visible : true / false
// },
// virtualPoints : {
// x : 0,
// y : 0
// },
// ...
// } ];
WorkflowDesigner.prototype._calcActiveLineEditingPoints = function() {
	var editingPoints = {};
	editingPoints.orgPoints = this.activeLine.transition.path.points;
	for ( var i in editingPoints.orgPoints) {
		if (editingPoints.orgPoints[i] instanceof Function)
			continue;

		if (i >= 1 && i <= editingPoints.orgPoints.length - 2) {
			editingPoints.orgPoints[i].visible = true;
		} else {
			editingPoints.orgPoints[i].visible = false;
		}
	}
	editingPoints.virtualPoints = new Array();
	for ( var i = 0; i <= editingPoints.orgPoints.length - 2; i++) {
		editingPoints.virtualPoints[i] = {};
		editingPoints.virtualPoints[i].x = (editingPoints.orgPoints[i].x + editingPoints.orgPoints[i + 1].x) / 2;
		editingPoints.virtualPoints[i].y = (editingPoints.orgPoints[i].y + editingPoints.orgPoints[i + 1].y) / 2;
	}

	this.activeLine.editingPoints = editingPoints;
};

WorkflowDesigner.prototype._updateTransition = function() {
	switch (this.editingPointToMove.type) {
	case 'orgPoint':
		// 移动原来的控制点
		var points = this.activeLine.transition.path.points;
		var point = points[this.editingPointToMove.index];
		point.x = this.editingPointToMove.destX;
		point.y = this.editingPointToMove.destY;
		this._calcActiveLineEditingPoints();
		this._buildTextLoc(this.activeLine.transition);
		break;
	case 'virtualPoint':
		// 新增控制点
		var points = this.activeLine.transition.path.points;
		console.log("===============");
		console.log(points);
		var newPoint = {
			x : this.editingPointToMove.destX,
			y : this.editingPointToMove.destY
		};
		points.splice(+this.editingPointToMove.index + 1, 0, newPoint);
		this._calcActiveLineEditingPoints();
		this._buildTextLoc(this.activeLine.transition);
		console.log(points);
		break;
	}
};
// 汇总流程的所有节点的权限
// ===========================
//WorkflowDesigner.prototype._getPrivilege = function() {
//	var privArray = new Array();
//	for ( var i in this.actiArray) {
//		if (this.actiArray[i] instanceof Function)
//			continue;
//
//		if (this.actiArray[i].tagName == 'task') {
//			if (this.actiArray[i].privilegeid) {
//				privArray.push(this.actiArray[i].name + ":"
//						+ this.actiArray[i].privilegeid);
//			}
//		}
//	}
//	return privArray.join(";");
//};

WorkflowDesigner.prototype._changeActiName = function(newName, acti) {

	if (this.editMode == 'UPDATE') {
		this.warn("已发布的工作流节点ID不能进行修改！");
		return;
	}
	// 更新名称
	var oldActiName = acti.name;
	acti.name = newName;
	this.actiMap[oldActiName] = undefined;
	this.actiMap[newName] = acti;
	// 更新到达acti的transition
	for ( var i in this.actiArray) {
		if (this.actiArray[i] instanceof Function)
			continue;

		for ( var j in this.actiArray[i].transition) {
			if (this.actiArray[i].transition[j] instanceof Function)
				continue;
			if (this.actiArray[i].transition[j].to == oldActiName) {
				this.actiArray[i].transition[j].to = newName;
				break;
			}
		}
	}
	// 更新扩展缓存

	for ( var i = 0; i < this.extAttrCache.length; i++) {
		var attr = this.extAttrCache[i];
		if (oldActiName == attr.srcacti) {
			attr.srcacti = newName;
		}
	}

};

WorkflowDesigner.prototype._showProperties = function(actiName) {
	this._propEndEdit();
	this._oldActiName = actiName;
	var actiType = this.actiMap[actiName].tagName;
	var propModel = this._getPropertyModel(actiType);
	this._fillData(this.actiMap[actiName], propModel);
	this._displayProperties(propModel);
};
/**
 * 更新扩展属性到extAttrCache缓存
 * 
 * @param category
 *            扩展属性类别
 * @param actiName
 *            活动名称
 * @param transName
 *            迁称线名称
 * @param fieldName
 *            属性名称
 * @param fieldValue
 *            属性值
 */
WorkflowDesigner.prototype._updateExtAttrProperty = function(category,
		actiName, transName, fieldName, fieldValue) {
	var tgtType = "ACTIVITY";
	if ("TRANS" == category.substring(0, 5)) {
		tgtType = "TRANSITION";
	}

	// 区分对待活动和迁移线，遍历方法不一致
	if (tgtType == "ACTIVITY") {
		for ( var i = 0; i < this.extAttrCache.length; i++) {
			if (this.extAttrCache[i].category == category
					&& this.extAttrCache[i].srcacti == actiName) {
				this.extAttrCache[i][fieldName] = fieldValue;
				break;
			}
		}
	} else {
		for ( var i = 0; i < this.extAttrCache.length; i++) {
			if (this.extAttrCache[i].category == category
					&& this.extAttrCache[i].srcacti == actiName
					&& this.extAttrCache[i].transition == transName) {
				this.extAttrCache[i][fieldName] = fieldValue;
				break;
			}
		}
	}
	// 立即提交到服务端
	if (this.editMode == "UPDATE")
		this._commitChangesToServer(category, actiName, transName, fieldName,
				fieldValue);
};
/**
 * 迁称线更名
 * 
 * @param actiName
 *            源活动节点名称
 * @param oldTransName
 *            原迁移线名称
 * @param newTransName
 *            新迁移线名称
 */
WorkflowDesigner.prototype._updateTransitionName = function(actiName,
		oldTransName, newTransName) {
	// 更新Acti所包含的transition的name
	var acti = this.actiMap[actiName];
	for ( var i = 0; i < acti.transition.length; i++) {
		if (acti.transition[i].name == oldTransName) {
			acti.transition[i].name = newTransName;
			break;
		}
	}
	// 更新扩展属性列表中的
	for ( var j = 0; j < this.extAttrCache.length; j++) {
		if (this.extAttrCache[j].srcacti == actiName
				&& this.extAttrCache[j].transition == oldTransName) {
			this.extAttrCache[j].transition = newTransName;
		}
	}
};
