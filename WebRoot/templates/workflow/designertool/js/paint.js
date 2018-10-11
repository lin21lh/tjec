function getContext() {
	if (local_context) {
		return local_context;
	}
	var canvas = document.getElementById("canvas")
	if (canvas == null)
		return null;
	local_context = canvas.getContext("2d");
	return local_context;
}

function repaintCanvas() {
	var context = getContext();
	context.clearRect(0, 0, context.canvas.width, context.canvas.height);

	drawLines();
	drawShapes();

	drawArrows();
	// 绘制焦点
	if (activeObjectName) {
		var shape = actiMap[activeObjectName];

		context.strokeStyle = 'black';
		context.lineWidth = 1;
		context.strokeRect(shape.x - 0.5, shape.y - 0.5, shape.w + 1,
				shape.h + 1);
	}

	if (activeLine) {
		drawActiveLinePoints(context);
	}
	
	if(virtualPoint.visible){
		drawVirtualPoint(context);
	}

}

function drawArrows() {
	var context = getContext();
	context.fillStyle = 'black';
	for ( var i in actiArray) {
		var node = actiArray[i];
		if (node instanceof Function)
			continue;
		
		for ( var j in node.transition) {
			var tran = node.transition[j];
			if (tran instanceof Function)
				continue;
			
			var arrow = getTransitionArrow(tran);
			var dest = actiMap[tran.to];

			context.moveTo(0, 0);
			context.save();
			context.translate(arrow.x + dest.centerX, arrow.y + dest.centerY);
			context.rotate(arrow.angle + Math.PI);
			context.beginPath();
			context.moveTo(-10, 0);
			context.lineTo(-10, 4);
			context.lineTo(0, 0);
			context.lineTo(-10, -4);
			context.lineTo(-10, 0);

			// context.arc(arrow.x+dest.centerX,arrow.y+dest.centerY,5,Math.PI*2,true,true);
			context.fill();
			context.restore();
		}
	}

}

function drawLines() {
	var context = getContext();
	for ( var i in actiArray) {
		var nd = actiArray[i];
		if (nd instanceof Function)
			continue;
		for ( var j in nd.transition) {
			if (nd.transition[j] instanceof Function)
				continue;
			
			drawLine(nd.transition[j], context);
		}
	}
}
function drawLine(transition, context) {
	// 路径
	prepareLineContext(context);
	var path = transition.path;
	if (path) {
		if (path.points) {

			context.beginPath();
			context.moveTo(path.points[0].x, path.points[0].y);
			for ( var i = 1; i < path.points.length; i++) {
				context.lineTo(path.points[i].x, path.points[i].y);
			}
			context.stroke();
		}
	}
	// 文本
	prepareTransitionTextContext(context);
	context.fillText(transition.name, transition.text.x, transition.text.y);

}

// 绘制所有的活动节点形状
function drawShapes() {
	var context = getContext();
	for ( var i in actiArray) {
		var shape = actiArray[i];
		if (shape instanceof Function)
			continue;
		
		switch (shape.tagName) {
		case 'task':
		case 'state':
			drawTask(shape.x, shape.y, shape.w, shape.h, shape.description, context);
			break;

		case 'decision':
		case 'rule':
			drawDecision(shape.x, shape.y, shape.w, shape.h, context);
			break;
		case 'fork':
		case 'join':
			drawFork(shape.x, shape.y, shape.w, shape.h, context);
			break;
		case 'start':
			drawStart(shape.x, shape.y, shape.w, shape.h, context);
			break;
		case 'end':
			drawEnd(shape.x, shape.y, shape.w, shape.h, context);
			break;
		}

	}

	if (drawVirtualBox.visible) {
		context.strokeStyle = 'blue';
		context.strokeRect(drawVirtualBox.x, drawVirtualBox.y,
				drawVirtualBox.w, drawVirtualBox.h);
	}

}
// 绘制task
function drawTask(x, y, w, h, text, context) {
	context.fillStyle = 'white';
	context.fillRect(x, y, w, h);
	prepareTaskContext(context);
	context.roundRect(x + 7, y + 7, w - 14, h - 14, 10, true, true);
	prepareTextContext(context);
	context.fillText(text, x + w / 2, y + h / 2);
}

function drawStart(x, y, w, h, context) {
	context.drawImage(imgs.start, x - 1, y - 2);
}

function drawEnd(x, y, w, h, context) {
	context.drawImage(imgs.end, x - 1, y - 2);
}

function drawDecision(x, y, w, h, context) {
	context.drawImage(imgs.decision, x - 1, y - 2);
}

function drawFork(x, y, w, h, context) {
	context.drawImage(imgs.fork, x - 1, y - 2);
}
/**
 * 绘制取得焦点的线的控制点
 * @param context
 */
function drawActiveLinePoints(context) {
	prepareActiveLineContext(context);
	var editingPts = activeLine.editingPoints;
	for ( var i in editingPts.orgPoints) {
		if (editingPts.orgPoints[i] instanceof Function)
			continue;
		
		if (editingPts.orgPoints[i].visible == true) {
			context.fillRect(editingPts.orgPoints[i].x - 2,
					editingPts.orgPoints[i].y - 2, 5, 5);
		}
	}

	for ( var i in editingPts.virtualPoints) {
		if (editingPts.virtualPoints[i] instanceof Function)
			continue;
		
		context.fillRect(editingPts.virtualPoints[i].x - 1,
				editingPts.virtualPoints[i].y - 1, 3, 3);

	}
}

function prepareActiveLineContext(context) {
	context.fillStyle = 'blue';
	context.strokeStyle = 'blue';
	context.lineWidth = 2;
}

function prepareTaskContext(context) {
	context.fillStyle = '#f6f7ff';
	context.strokeStyle = '#03689a';
	context.lineWidth = 2;
}

function prepareTextContext(context) {
	context.fillStyle = 'black';
	context.strokeStyle = 'black';
	context.lineWidth = 1;
	context.font = 'normal 12px 微软雅黑';
	context.textAlign = 'center';
	context.textBaseline = 'middle';
}

function prepareTransitionTextContext(context) {
	context.fillStyle = 'black';
	context.lineWidth = 1;
	context.font = 'normal 12px 微软雅黑';
	context.textAlign = 'start';
	context.textBaseline = 'top';
}
function prepareLineContext(context) {
	context.fillStyle = 'gray';
	context.strokeStyle = 'gray';
	context.lineWidth = 1;
	context.font = 'normal 12px 微软雅黑';
	context.textAlign = 'start';
	context.textBaseline = 'top';
}

function drawVirtualPoint(context){
	
	console.log('drawing vpoints');
	context.strokeStyle = 'gray';
	context.lineWidth = 1;
	context.beginPath();
	context.moveTo(virtualPoint.x-1000,virtualPoint.y+0.5);
	context.lineTo(virtualPoint.x+1000,virtualPoint.y+0.5);
	context.stroke();
	context.beginPath();
	context.moveTo(virtualPoint.x+0.5,virtualPoint.y-1000);
	context.lineTo(virtualPoint.x+0.5,virtualPoint.y+1000);
	context.stroke();
//	context.fillRect(virtualPoint.x-2,virtualPoint.y-2,5,5);
}
