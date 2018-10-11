JbfFormDesigner.prototype.paint = function() {
	this.context.clearRect(0, 0, this.context.canvas.width,
			this.context.canvas.height);
	
	this.context.strokeStyle='#95B8E7';
	this.context.beginPath();
	this.context.moveTo(this.offset.x-10.5,0);
	this.context.lineTo(this.offset.x-10.5,2000);
	this.context.stroke();
	this.paintLabels(this.context);
	this.paintControls(this.context);
	this.paintSelectedControl(this.context);
	this.paintTempLocation(this.context);
	this.paintOptionalControls(this.context);
	this.paintInteract(this.context);
	this.paintTrashTool(this.context);
};

// 绘制标签
JbfFormDesigner.prototype.paintLabels = function(context) {

	context.fillStyle = 'black';
	context.strokeStyle = '#95B8E7';
	context.lineWidth = 1;
	context.font = 'normal 12px 微软雅黑';
	context.textAlign = 'start';
	context.textBaseline = 'top';
	if(!this.dataModel.controls){
		return;
	}
	for ( var i = 0; i < this.dataModel.controls.length; i++) {
		
		var xp = this.dataModel.controls[i].col;
		var yp = this.dataModel.controls[i].row;

		var xpos = xp
				* (this.dataModel.labelCellWidth + this.dataModel.controlCellWidth)
				+ this.offset.x;
		var ypos = yp * this.dataModel.controlCellHeight + this.offset.y;

		var width_extend = 0;
		if (this.dataModel.controls[i].colspan) {
			width_extend = ((+this.dataModel.controls[i].colspan) - 1)
					* (this.dataModel.controlCellWidth + this.dataModel.labelCellWidth);
		}
		context.fillText(this.dataModel.controls[i].label + "：", xpos + 10,
				ypos + 5);

		context.strokeRect(xpos + 0.5, ypos + 0.5,
				this.dataModel.labelCellWidth + this.dataModel.controlCellWidth
						+ width_extend, this.dataModel.controlCellHeight);
	}

};

// 绘制已选控件
JbfFormDesigner.prototype.paintControls = function(context) {

	context.strokeStyle = '#95B8E7';
	context.lineWidth = 1;
	context.textAlign = 'start';
	context.textBaseline = 'top';
	if(!this.dataModel.controls){
		return;
	}
	for ( var i = 0; i < this.dataModel.controls.length; i++) {
		var xp = this.dataModel.controls[i].col;
		var yp = this.dataModel.controls[i].row;
		var width_extend = 0;
		if (this.dataModel.controls[i].colspan) {
			width_extend = ((+this.dataModel.controls[i].colspan) - 1)
					* (this.dataModel.controlCellWidth + this.dataModel.labelCellWidth);
		}

		var xpos = xp
				* (this.dataModel.labelCellWidth + this.dataModel.controlCellWidth)
				+ this.offset.x + this.dataModel.labelCellWidth + 0.5;
		var ypos = yp
				* 30
				+ this.offset.y
				+ 0.5
				+ (this.dataModel.controlCellHeight - this.dataModel.controlHeight)
				/ 2;
		// 绘制控件边框
		context.strokeRect(xpos, ypos, this.dataModel.controlWidth - 1
				+ width_extend, this.dataModel.controlHeight);
		// 绘制控件的图标
		var imgx = this.getImageByType(this.dataModel.controls[i].type);
		if (imgx) {
			context.drawImage(imgx, xpos + this.dataModel.controlWidth - 18.5
					+ width_extend, ypos + 1.5);
		}
	}
};
// 绘制可选的控件
JbfFormDesigner.prototype.paintOptionalControls = function(context) {

	context.lineWidth = 1;
	context.textAlign = 'start';
	context.textBaseline = 'top';
	var octl = this.dataModel.optionalControls;
	
	if(!octl){
		return;
	}
	var firstIndex = this.variables.opCtlPageSize * this.variables.opCtlCurPage;

	var lastIndex = this.variables.opCtlPageSize
			* (this.variables.opCtlCurPage + 1) - 1;
	if (lastIndex >= octl.length) {
		lastIndex = octl.length - 1;
	}

	for ( var i = firstIndex; i <= lastIndex; i++) {
		var pos = i - firstIndex;
		if (octl[pos].used) {
			context.fillStyle = '#EEEEEE';
		} else {
			context.fillStyle = '#E0ECFF';
		}

		context.fillRect(this.offset.opx, pos * 30 + this.offset.y, 150, 28);
		context.fillStyle = 'black';
		context.fillText(octl[pos].label, this.offset.opx + 10, pos * 30
				+ this.offset.y + 5);
	}
};

// 绘制焦点控件
JbfFormDesigner.prototype.paintSelectedControl = function(context) {

	if (this.activeObject.activeControl != null) {
		context.strokeStyle = 'red';
		context.lineWidth = 2;
		var xpos = this.activeObject.activeControl.col
				* (this.dataModel.labelCellWidth + this.dataModel.controlCellWidth)
				+ this.offset.x;
		var ypos = this.activeObject.activeControl.row
				* this.dataModel.controlCellHeight + this.offset.y;

		var width_extend = 0;
		if (this.activeObject.activeControl.colspan) {
			width_extend = ((+this.activeObject.activeControl.colspan) - 1)
					* (this.dataModel.controlCellWidth + this.dataModel.labelCellWidth);
		}

		context.strokeRect(xpos, ypos, this.dataModel.labelCellWidth
				+ this.dataModel.controlCellWidth + width_extend,
				this.dataModel.controlCellHeight);
	}
}
// 绘制放置控件前的交互框
JbfFormDesigner.prototype.paintTempLocation = function(context) {

	if (this.activeObject.tempLocation.row != null
			&& this.activeObject.tempLocation.col != null) {
		context.strokeStyle = 'red';
		context.lineWidth = 1;
		var control = this.getControlByPosition(
				this.activeObject.tempLocation.row,
				this.activeObject.tempLocation.col);
		if (control) {
			// 如果鼠指针下面是控件
			var width_extend = 0;
			if (control.colspan) {
				width_extend = ((+control.colspan) - 1)
						* (this.dataModel.controlCellWidth + this.dataModel.labelCellWidth);
			}

			var xpos = control.col
					* (this.dataModel.labelCellWidth + this.dataModel.controlCellWidth)
					+ this.offset.x;
			var ypos = control.row * this.dataModel.controlCellHeight
					+ this.offset.y;

			context.strokeRect(xpos + 0.5, ypos + 0.5,
					this.dataModel.labelCellWidth
							+ this.dataModel.controlCellWidth + width_extend,
					this.dataModel.controlCellHeight);

		} else {
			// 如果鼠标指针下面没有控件
			var xpos = this.activeObject.tempLocation.col
					* (this.dataModel.labelCellWidth + this.dataModel.controlCellWidth)
					+ this.offset.x;
			var ypos = this.activeObject.tempLocation.row
					* this.dataModel.controlCellHeight + this.offset.y;
			context.strokeRect(xpos + 0.5, ypos + 0.5,
					this.dataModel.labelCellWidth
							+ this.dataModel.controlCellWidth,
					this.dataModel.controlCellHeight);
		}

	}
};

// 渲染取用待选控件后的交互图标
JbfFormDesigner.prototype.paintInteract = function(context) {
	if (this.flags.controlCreating) {
		this.context.drawImage(this.imgs.controlCreating,
				this.variables.mouseStartX - 16,
				this.variables.mouseStartY - 16);
	}
};

JbfFormDesigner.prototype.paintTrashTool = function(context) {
	if (this.flags.trashVisible) {
		context.fillStyle = 'gray';
		context.strokeStyle = 'gray';
		this.context.fillText('拖放控件到这里删除!', 50, 340);
		this.context.drawImage(this.imgs.trash, 70, this.offset.y + 340);
		this.context.strokeRect(40, 330, 130, 120);
	}
};
