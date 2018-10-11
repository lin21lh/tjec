WorkflowDesigner.prototype._showWorkflowChart = function() {
	this._repaintCanvas();
};

WorkflowDesigner.prototype._getContext = function() {
	return this.context;
};

WorkflowDesigner.prototype._repaintCanvas = function() {
	var context = this._getContext();
	context.clearRect(0, 0, context.canvas.width, context.canvas.height);

	this._drawLines();
	this._drawShapes();

	this._drawArrows();
	// 绘制焦点
	if (this.activeObjectName) {
		var shape = this.actiMap[this.activeObjectName];
		if (shape) {
			context.strokeStyle = 'black';
			context.lineWidth = 1;
			context.strokeRect(shape.x - 0.5, shape.y - 0.5, shape.w + 1,
					shape.h + 1);
		}
	}

	if (this.activeLine) {
		this._drawActiveLinePoints(context);
	}

	if (this.virtualPoint.visible) {
		this._drawVirtualPoint(context);
	}
};

WorkflowDesigner.prototype._drawArrows = function() {
	var context = this._getContext();
	context.fillStyle = 'black';
	for ( var i in this.actiArray) {
		var node = this.actiArray[i];
		if (node instanceof Function)
			continue;

		for ( var j in node.transition) {
			var tran = node.transition[j];
			if (tran instanceof Function)
				continue;

			var arrow = this._getTransitionArrow(tran);
			var dest = this.actiMap[tran.to];

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
};

WorkflowDesigner.prototype._drawLines = function() {
	var context = this._getContext();
	for ( var i in this.actiArray) {
		var nd = this.actiArray[i];
		if (nd instanceof Function)
			continue;
		for ( var j in nd.transition) {
			if (nd.transition[j] instanceof Function)
				continue;

			this._drawLine(nd.transition[j], context);
		}
	}
};
WorkflowDesigner.prototype._drawLine = function(transition, context) {
	// 路径
	this._prepareLineContext(context);
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
	this._prepareTransitionTextContext(context);
	context.fillText(transition.name, transition.text.x, transition.text.y);

};

// 绘制所有的活动节点形状
WorkflowDesigner.prototype._drawShapes = function() {
	var context = this._getContext();
	for ( var i in this.actiArray) {
		var shape = this.actiArray[i];
		if (shape instanceof Function)
			continue;

		switch (shape.tagName) {
		case 'task':
		case 'state':
			this._drawTask(shape.x, shape.y, shape.w, shape.h,
					shape.description, context);
			break;

		case 'decision':
		case 'rule':
			this._drawDecision(shape.x, shape.y, shape.w, shape.h, context);
			break;
		case 'fork':
		case 'join':
			this._drawFork(shape.x, shape.y, shape.w, shape.h, context);
			break;
		case 'start':
			this._drawStart(shape.x, shape.y, shape.w, shape.h, context);
			break;
		case 'end':
			this._drawEnd(shape.x, shape.y, shape.w, shape.h, context);
			break;
		}

	}

	if (this.drawVirtualBox.visible) {
		context.strokeStyle = 'blue';
		context.strokeRect(this.drawVirtualBox.x, this.drawVirtualBox.y,
				this.drawVirtualBox.w, this.drawVirtualBox.h);
	}

};
// 绘制task
WorkflowDesigner.prototype._drawTask = function(x, y, w, h, text, context) {
	context.fillStyle = 'white';
	context.fillRect(x, y, w, h);
	this._prepareTaskContext(context);
	context.roundRect(x + 7, y + 7, w - 14, h - 14, 10, true, true);
	this._prepareTextContext(context);
	context.fillText(text, x + w / 2, y + h / 2);
};

WorkflowDesigner.prototype._drawStart = function(x, y, w, h, context) {
	context.drawImage(this.imgs.start, x - 1, y - 2);
};

WorkflowDesigner.prototype._drawEnd = function(x, y, w, h, context) {
	context.drawImage(this.imgs.end, x - 1, y - 2);
};

WorkflowDesigner.prototype._drawDecision = function(x, y, w, h, context) {
	context.drawImage(this.imgs.decision, x - 1, y - 2);
};

WorkflowDesigner.prototype._drawFork = function(x, y, w, h, context) {
	context.drawImage(this.imgs.fork, x - 1, y - 2);
};
/**
 * 绘制取得焦点的线的控制点
 * 
 * @param context
 */
WorkflowDesigner.prototype._drawActiveLinePoints = function(context) {
	this._prepareActiveLineContext(context);
	var editingPts = this.activeLine.editingPoints;
	if (editingPts) {
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
};

WorkflowDesigner.prototype._prepareActiveLineContext = function(context) {
	context.fillStyle = 'blue';
	context.strokeStyle = 'blue';
	context.lineWidth = 2;
};

WorkflowDesigner.prototype._prepareTaskContext = function(context) {
	context.fillStyle = '#f6f7ff';
	context.strokeStyle = '#03689a';
	context.lineWidth = 2;
};

WorkflowDesigner.prototype._prepareTextContext = function(context) {
	context.fillStyle = 'black';
	context.strokeStyle = 'black';
	context.lineWidth = 1;
	context.font = 'normal 12px 微软雅黑';
	context.textAlign = 'center';
	context.textBaseline = 'middle';
};

WorkflowDesigner.prototype._prepareTransitionTextContext = function(context) {
	context.fillStyle = 'black';
	context.lineWidth = 1;
	context.font = 'normal 12px 微软雅黑';
	context.textAlign = 'start';
	context.textBaseline = 'top';
};
WorkflowDesigner.prototype._prepareLineContext = function(context) {
	context.fillStyle = 'gray';
	context.strokeStyle = 'gray';
	context.lineWidth = 1;
	context.font = 'normal 12px 微软雅黑';
	context.textAlign = 'start';
	context.textBaseline = 'top';
};

WorkflowDesigner.prototype._drawVirtualPoint = function(context) {
	context.strokeStyle = 'gray';
	context.lineWidth = 1;
	context.beginPath();
	context.moveTo(this.virtualPoint.x - 1000, this.virtualPoint.y + 0.5);
	context.lineTo(this.virtualPoint.x + 1000, this.virtualPoint.y + 0.5);
	context.stroke();
	context.beginPath();
	context.moveTo(this.virtualPoint.x + 0.5, this.virtualPoint.y - 1000);
	context.lineTo(this.virtualPoint.x + 0.5, this.virtualPoint.y + 1000);
	context.stroke();
	// context.fillRect(virtualPoint.x-2,virtualPoint.y-2,5,5);
};
