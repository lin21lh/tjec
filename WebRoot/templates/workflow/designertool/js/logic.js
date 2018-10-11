/*******************************************************************************
 * 事件处理
 * 
 ******************************************************************************/
function mouseDown(e) {
	console.log('mouse down');
	mouseDownLoc.x = e.offsetX;
	mouseDownLoc.y = e.offsetY;
	mousePressed = true;
	switch (opMode) {
	case 'select':
		for ( var i in actiArray) {
			var node = actiArray[i];
			if (node instanceof Function)
				continue;
			// 判断是否有物体被选中
			if (e.offsetX >= node.x && e.offsetY >= node.y
					&& e.offsetX <= (node.x + node.w)
					&& e.offsetY <= (node.y + node.h)) {
				if (node.name != activeObjectName) {

					console.log('Gained Foucs : ' + node.name);
					activeObjectName = node.name;
					activeLine = null;
					showProperties(activeObjectName);
				}
				repaintCanvas();
				return;
			}
		}

		for ( var i in actiArray) {
			if (actiArray[i] instanceof Function)
				continue;
			var trans = actiArray[i].transition;

			for ( var j in trans) {
				var tran = trans[j];
				if (tran instanceof Function)
					continue;
				for ( var k = 0; k < tran.path.points.length - 1; k++) {
					if (inline(e.offsetX, e.offsetY, tran.path.points[k].x,
							tran.path.points[k].y, tran.path.points[k + 1].x,
							tran.path.points[k + 1].y)) {

						activeLine = {};
						activeLine.name = tran.name;
						activeLine.transition = tran;
						activeLine.from = actiArray[i];
						activeLine.to = tran.to;

						calcActiveLineEditingPoints();
						continueFlag = false;
						activeObjectName = null;
						console.log(" line selected!");
						console.log(activeLine);
						showTransitionProp(activeLine);
						repaintCanvas();
						return;
					}
				}
			}
		}
		activeObjectName = null;
		activeLine = null;

		showProcessProp();
		break;
	case 'transitionCreate':
		transitionDraw.src.name = null;
		for ( var i in actiArray) {
			var node = actiArray[i];
			if (node instanceof Function)
				continue;
			if (e.offsetX >= node.x && e.offsetY >= node.y
					&& e.offsetX <= (node.x + node.w)
					&& e.offsetY <= (node.y + node.h)) {
				transitionDraw.src.name = node.name;
				transitionDraw.src.x = node.centerX;
				transitionDraw.src.y = node.centerY;
				break;
			}
		}

		break;
	case 'activityCreate':
		activeObjectName = createActivity(toolbox.widget, e.offsetX, e.offsetY);
		showProperties(activeObjectName);
		break;
	}
	repaintCanvas();

}

function mouseUp(e) {
	console.log('mouse up');
	mousePressed = false;
	virtualPoint.visible = false;
	switch (opMode) {
	case 'select':

		drawVirtualBox.visible = false;
		var movementX = e.offsetX - mouseDownLoc.x;
		var movementY = e.offsetY - mouseDownLoc.y;

		if (movementX != 0 || movementY != 0) {
			if (activeObjectName) {
				moveActivity(activeObjectName, movementX, movementY);
				showProperties(activeObjectName);
				repaintCanvas();
			} else if (activeLine) {
				if (editingPointToMove.type != null) {
					editingPointToMove.destX = e.offsetX;
					editingPointToMove.destY = e.offsetY;
					updateTransition();
					repaintCanvas();
					showTransitionProp(activeLine);
				}
			}
		}

		break;
	case 'transitionCreate':
		console.log("transitionCreate - mouse up");
		transitionDraw.dest.name = null;
		for ( var i in actiArray) {
			var node = actiArray[i];
			if (node instanceof Function)
				continue;
			
			if (e.offsetX >= node.x && e.offsetY >= node.y
					&& e.offsetX <= (node.x + node.w)
					&& e.offsetY <= (node.y + node.h)) {
				transitionDraw.dest.name = node.name;
				transitionDraw.dest.x = node.centerX;
				transitionDraw.dest.y = node.centerY;
				transitionDraw.dest.description=node.description;
				break;
			}
		}
		console.log("transitionCreate - mouse up 2");
		// 起始和目标都有组件
		if (transitionDraw.src.name != null && transitionDraw.dest.name != null
				&& transitionDraw.src.name != transitionDraw.dest.name) {
			addTransition(transitionDraw);
		}
		repaintCanvas();
		break;
	case 'activityCreate':
		$('#widgets').datagrid('selectRow', 0);
		opMode = 'select';
		break;
	}
}

function mouseMove(e) {
	switch (opMode) {
	case 'select':
		if (mousePressed) {

			if (activeObjectName) {
				var movementX = e.offsetX - mouseDownLoc.x;
				var movementY = e.offsetY - mouseDownLoc.y;

				var shape = actiMap[activeObjectName];

				drawVirtualBox.visible = true;
				drawVirtualBox.x = shape.x + movementX;
				drawVirtualBox.y = shape.y + movementY;
				drawVirtualBox.w = shape.w;
				drawVirtualBox.h = shape.h;
				repaintCanvas();
			} else if (activeLine) {
				virtualPoint.visible = true;
				virtualPoint.x = e.offsetX;
				virtualPoint.y = e.offsetY;
				repaintCanvas();
			}
		} else if (activeLine) {
			for ( var i in activeLine.editingPoints.orgPoints) {
				var p = activeLine.editingPoints.orgPoints[i];
				if (p instanceof Function)
					continue;
				if ((p.x - e.offsetX) * (p.x - e.offsetX) + (p.y - e.offsetY)
						* (p.y - e.offsetY) <= 16) {
					getContext().canvas.style.cursor = 'crosshair';

					editingPointToMove.type = 'orgPoint';
					editingPointToMove.index = i;
					return;
				}
			}

			for ( var i in activeLine.editingPoints.virtualPoints) {
				var p = activeLine.editingPoints.virtualPoints[i];
				if (p instanceof Function)
					continue;
				if ((p.x - e.offsetX) * (p.x - e.offsetX) + (p.y - e.offsetY)
						* (p.y - e.offsetY) <= 16) {
					getContext().canvas.style.cursor = 'crosshair';
					editingPointToMove.type = 'virtualPoint';
					editingPointToMove.index = i;
					return;
				}
			}
			editingPointToMove.type = null;
			editingPointToMove.index = -1;
			getContext().canvas.style.cursor = 'default';

		}
		break;
	case 'transitionCreate':

		break;
	case 'activityCreate':
		break;
	}
}

function designer_canvas_keyDown(e) {
	switch (opMode) {
	case 'select':
		var keyCode = e.keyCode ? e.keyCode : e.which;
		if (keyCode == 46) {
			if (activeObjectName != null && propEditing == false) {
				console.log("deleting activity " + activeObjectName);
				removeActivity(activeObjectName);
			} else if (activeLine != null && propEditing == false) {

				if (editingPointToMove.type == null) {
					// 删除整条线
					console.log("deleting transition " + activeLine.name
							+ " from " + activeLine.from);
					removeTransition(activeLine);
					activeLine = null;
				} else if (editingPointToMove.type == 'orgPoint') {
					// 移除控制点
					var acti = activeLine.from;

					for ( var i in acti.transition) {
						if (acti.transition[i] instanceof Function)
							continue;
						if (acti.transition[i].name == activeLine.name) {
							var points = acti.transition[i].path.points;
							points.splice(+editingPointToMove.index, 1);
							calcActiveLineEditingPoints();
							buildTextLoc(acti.transition[i]);
						}
					}
				}
			}
			repaintCanvas();
		}

		break;
	}

}

/*******************************************************************************
 * 操作逻辑部分
 * 
 ******************************************************************************/
// 移动Activity节点
function moveActivity(actiName, movX, movY) {
	var curActi = actiMap[actiName];
	curActi.x += movX;
	curActi.y += movY;

	curActi.centerX += movX;
	curActi.centerY += movY;
	updateActivityTransitions(curActi);
}

function updateActivityTransitions(acti) {
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
			buildTextLoc(tran);
		}
	}
	// 2.2 income transition

	for ( var i in actiArray) {
		var node = actiArray[i];
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
				buildTextLoc(tran);
			}
		}

	}
}

function createActivity(widgetName, x, y) {
	var newName = getNextActivityName(widgetName);
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
		description:newName
	};

	if (widgetName == 'task') {
		newActi.assignmentHandler = default_assignment_handler;
	}

	actiArray.push(newActi);
	actiMap[newName] = newActi;
	return newName;
}

function getNextActivityName(tagName) {
	var nextNum = 1;
	for ( var i in actiArray) {
		if (actiArray[i] instanceof Function)
			continue;
		if (actiArray[i].name.indexOf(tagName) == 0) {
			var otherName = actiArray[i].name.substring(tagName.length);

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
}
// 迁移线位置
function buildTextLoc(tran) {
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

}

function getTransitionArrow(transition) {
	var destShape = actiMap[transition.to];

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

}
/**
 * 新增迁移线
 * 
 * @param transDraw
 */
function addTransition(transDraw) {

	console.log('Adding Transistion!');
	var tran = {
		name : '发送至' + transDraw.dest.description,
		to : transDraw.dest.name,
		g : '-76,-12'
	};
	var oldTrans = actiMap[transDraw.src.name].transition;
	for ( var i in oldTrans) {
		if (oldTrans[i] instanceof Function)
			continue;
		
		if (oldTrans[i].to == tran.to) {
			console.log("路径已存在，不能重复添加！");
			return;
		}
	}

	buildTransitionStruct(tran, actiMap[transDraw.src.name]);
	if (undefined == actiMap[transDraw.src.name].transition) {
		actiMap[transDraw.src.name].transition = new Array();
	}
	actiMap[transDraw.src.name].transition.push(tran);

}

/**
 * 建立迁移线的结构
 * 
 * @param tran
 */
function buildTransitionStruct(tran, srcActi) {
	
	if (tran instanceof Function)
		return;
	
	tran.path = {};
	tran.path.orgX = srcActi.centerX;
	tran.path.orgY = srcActi.centerY;

	var dest = actiMap[tran.to];

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

		buildTextLoc(tran);

	}
}

function removeTransition(activeLineArg) {

	var acti = activeLineArg.from;
	if (acti) {
		var trans = acti.transition;
		for ( var i = 0; i < trans.length; i++) {
			if (trans[i].to == activeLineArg.to) {
				trans.splice(i, 1);
			}
		}
	}

}

function removeActivity(actiName) {
	// 先删除到达activity的迁移线

	for ( var i in actiArray) {
		var activityNode = actiArray[i];
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
	actiMap[actiName] = undefined;
	for ( var i in actiArray) {
		if (actiArray[i] instanceof Function)
			continue;
		
		if (actiArray[i].name == actiName) {
			actiArray.splice(i, 1);
		}
	}
}

function changeProperties() {
	var rows = $('#prop').propertygrid('getRows');

	if (activeObjectName != null) {
		console.log("====== on change ============= " + activeObjectName);
		var acti = actiMap[activeObjectName];
		if (acti) {
			for ( var i in rows) {
				switch (rows[i].field) {
				case 'x':
				case 'y':
				case 'w':
				case 'h':
					acti[rows[i].field] = +rows[i].value;
					break;
				case 'name':
					var newName = rows[i].value;

					if (newName != oldActiName) {
						// 更新名称
						changeActiName(oldActiName, newName, acti);
					}
					acti[rows[i].field] = newName;

					break;
				default:
					acti[rows[i].field] = rows[i].value;
					break;
				}

			}
			updateActi(acti);
		}
	} else if (activeLine) {
		var rows = $('#prop').propertygrid('getRows');
		var map_ = new Array();
		for ( var j in rows) {
			if (rows[j] instanceof Function)
				continue;
			
			map_[rows[j].field] = rows[j].value;
		}

		var trans = activeLine.from.transition;
		for ( var i in trans) {
			if (trans[i] instanceof Function)
				continue;
			if (trans[i].name == activeLine.name) {

				trans[i].name = map_['name'];
				activeLine.name = map_['name'];
				trans[i].text.relativeX = +map_['x'];
				trans[i].text.relativeY = +map_['y'];
				if (map_['expr']) {
					trans[i].expr = map_['expr'];
				} else {
					trans[i].expr = undefined;
				}

				if (map_['event']) {
					trans[i].event = map_['event'];
				} else {
					trans[i].event = undefined;
				}
				if (map_['args']) {
					trans[i].args = map_['args'];
				} else {
					trans[i].args = undefined;
				}
				buildTextLoc(trans[i]);
				break;
			}
		}

	} else {

		// 修改流程信息
		for ( var i in rows) {
			if (rows[i] instanceof Function)
				continue;
			
			processInfo[rows[i].field] = rows[i].value;
		}

		// 提交启用日期、停用日期到后台保存

		$.post(urls.editWorkflowProcVersion, processInfo, function(msg) {
			easyui_auto_notice(msg,null,null,"操作时发生异常！");
		}, "JSON");
	}
	repaintCanvas();

}

function updateActi(acti) {
	acti.centerX = acti.x + acti.w / 2;
	acti.centerY = acti.y + acti.h / 2;

	updateActivityTransitions(acti);
}
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
function calcActiveLineEditingPoints() {
	var editingPoints = {};
	editingPoints.orgPoints = activeLine.transition.path.points;
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

	activeLine.editingPoints = editingPoints;
}

function updateTransition() {
	switch (editingPointToMove.type) {
	case 'orgPoint':
		// 移动原来的控制点
		var points = activeLine.transition.path.points;
		var point = points[editingPointToMove.index];
		point.x = editingPointToMove.destX;
		point.y = editingPointToMove.destY;
		calcActiveLineEditingPoints();
		buildTextLoc(activeLine.transition);
		break;
	case 'virtualPoint':
		// 新增控制点
		var points = activeLine.transition.path.points;
		console.log("===============");
		console.log(editingPointToMove);
		console.log(points);
		var newPoint = {
			x : editingPointToMove.destX,
			y : editingPointToMove.destY
		};
		points.splice(+editingPointToMove.index + 1, 0, newPoint);
		calcActiveLineEditingPoints();
		buildTextLoc(activeLine.transition);
		console.log(points);
		break;
	}
}
// 汇总流程的所有节点的权限
function getPrivilege() {
	var privArray = new Array();
	for ( var i in actiArray) {
		if (actiArray[i] instanceof Function)
			continue;
		
		if (actiArray[i].tagName == 'task') {
			if (actiArray[i].privilegeid) {
				privArray.push(actiArray[i].name + ":"
						+ actiArray[i].privilegeid);
			}
		}
	}
	return privArray.join(";");
}

function changeActiName(oldActiName, newName, acti) {
	// 更新名称
	acti.name = newName;
	actiMap[oldActiName] = undefined;
	actiMap[newName] = acti;
	// 更新到达acti的transition

	for ( var i in actiArray) {
		if (actiArray[i] instanceof Function)
			continue;
		
		for ( var j in actiArray[i].transition) {
			if (actiArray[i].transition[j] instanceof Function)
				continue;
			if (actiArray[i].transition[j].to == oldActiName) {
				actiArray[i].transition[j].to = newName;
				break;
			}
		}
	}
}
