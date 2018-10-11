/*******************************************************************************
 * 鼠标按下单击事件
 ******************************************************************************/
JbfFormDesigner.prototype.mouseDown = function(e) {
	// alert(e.offsetX +","+e.offsetY);
	var mx = e.offsetX;
	var my = e.offsetY;
	// 记录到全局变量
	window.formdesigner_.variables.mouseStartX = e.offsetX;
	window.formdesigner_.variables.mouseStartY = e.offsetY;

	if (mx <= window.formdesigner_.offset.x) {
		// 判断是否选中了左边待选区域的控件

		var wx = mx - window.formdesigner_.offset.opx;
		var wy = my - window.formdesigner_.offset.y;
		if (wx < 150) {
			var index = Math.floor(wy / 30);
			// 当前页控件数
			var curPageTotal = window.formdesigner_.variables.opCtlTotal
					- window.formdesigner_.variables.opCtlPageSize
					* window.formdesigner_.variables.opCtlCurPage;
			if (index < curPageTotal && index >= 0) {
				var realIndex = window.formdesigner_.variables.opCtlPageSize
						* window.formdesigner_.variables.opCtlCurPage + index;
				if (window.formdesigner_.dataModel.optionalControls && window.formdesigner_.dataModel.optionalControls[realIndex] != null) {

					if (window.formdesigner_.dataModel.optionalControls[realIndex].used) {
						// 选中的是已用的控件
						window.formdesigner_.flags.mouseStartObjType = 'unopcontrol';
					} else {
						window.formdesigner_.flags.mouseStartObjType = 'opcontrol';
						console.log('选中:opcontrol' + index);
						// 保存选中的字段
						window.formdesigner_.variables.creatingCtlIndex = realIndex;
					}
				}

			} else {
				window.formdesigner_.variables.creatingCtlIndex = -1;
			}

		}
		window.formdesigner_.activeObject.activeControl = null;
	} else {
		// 判断是否选中了表单区域的控件
		var wx = mx - window.formdesigner_.offset.x;
		var wy = my - window.formdesigner_.offset.y;
		var selRow = Math.floor(wy
				/ window.formdesigner_.dataModel.controlCellHeight);
		var selCol = Math
				.floor(wx
						/ (window.formdesigner_.dataModel.controlCellWidth + window.formdesigner_.dataModel.labelCellWidth));

		var control = window.formdesigner_.getControlByPosition(selRow, selCol);
		if (control) {

			window.formdesigner_.activeObject.activeControl = control;
			console.log('选中:' + control.name);
			window.formdesigner_.loadProperty(
					window.formdesigner_.propertyTemplate.common, control);
			window.formdesigner_.flags.mouseStartObjType = 'control';

		} else {
			window.formdesigner_.activeObject.activeControl = null;

			// 加载表单属性数据
			window.formdesigner_.loadProperty(
					window.formdesigner_.propertyTemplate.form,
					window.formdesigner_.properties.form);
			window.formdesigner_.flags.mouseStartObjType = 'blank';
		}

	}

	window.formdesigner_.paint();

};

/*******************************************************************************
 * 鼠标移动事件
 ******************************************************************************/
JbfFormDesigner.prototype.mouseMove = function(e) {

	window.formdesigner_.variables.mouseStartX = e.offsetX;
	window.formdesigner_.variables.mouseStartY = e.offsetY;

	switch (window.formdesigner_.flags.mouseStartObjType) {

	case 'opcontrol': // 移动的是可选控件
		window.formdesigner_.flags.controlCreating = true;
		window.formdesigner_.context.canvas.style.cursor = 'move';

		// 判断是否进入了可放置的区域

		var wx = e.offsetX - window.formdesigner_.offset.x;
		var wy = e.offsetY - window.formdesigner_.offset.y;
		var selRow = Math.floor(wy
				/ window.formdesigner_.dataModel.controlCellHeight);
		var selCol = Math
				.floor(wx
						/ (window.formdesigner_.dataModel.controlCellWidth + window.formdesigner_.dataModel.labelCellWidth));
		if (selRow >= 0 && selCol >= 0) {
			window.formdesigner_.activeObject.tempLocation.row = selRow;
			window.formdesigner_.activeObject.tempLocation.col = selCol;

			var control = window.formdesigner_.getControlByPosition(selRow,
					selCol);
			if (control) {
				window.formdesigner_.context.canvas.style.cursor = 'not-allowed';
			}
		} else {
			window.formdesigner_.activeObject.tempLocation.row = null;
			window.formdesigner_.activeObject.tempLocation.col = null;
		}

		window.formdesigner_.paint();
		break;
	case 'unopcontrol': // 移动的是不可选控件
		window.formdesigner_.context.canvas.style.cursor = 'not-allowed';
		break;

	case 'control': // 移动的是表单区域的控件

		var wx = e.offsetX - window.formdesigner_.offset.x;
		var wy = e.offsetY - window.formdesigner_.offset.y;
		var selRow = Math.floor(wy
				/ window.formdesigner_.dataModel.controlCellHeight);
		var selCol = Math
				.floor(wx
						/ (window.formdesigner_.dataModel.controlCellWidth + window.formdesigner_.dataModel.labelCellWidth));

		if (selRow >= 0 && selCol >= 0) {
			console.log("mark 90 :" + selRow + "," + selCol);
			window.formdesigner_.flags.controlReordering = true;
			window.formdesigner_.activeObject.tempLocation.row = selRow;
			window.formdesigner_.activeObject.tempLocation.col = selCol;
		} else {
			window.formdesigner_.flags.controlReordering = false;
			window.formdesigner_.activeObject.tempLocation.row = null;
			window.formdesigner_.activeObject.tempLocation.col = null;
		}

		var control = window.formdesigner_.getControlByPosition(selRow, selCol);
		if (control) {
			window.formdesigner_.context.canvas.style.cursor = 'not-allowed';
		} else {
			window.formdesigner_.context.canvas.style.cursor = 'move';
		}

		window.formdesigner_.flags.trashVisible = true;

		window.formdesigner_.paint();
		break;
	}

};

/*******************************************************************************
 * 鼠标释放事件
 ******************************************************************************/
JbfFormDesigner.prototype.mouseUp = function(e) {
	// 是否是放置操作
	if (window.formdesigner_.activeObject.tempLocation.row != null
			&& window.formdesigner_.activeObject.tempLocation.col != null) {
		if (window.formdesigner_.flags.controlCreating) {
			// 取得源字段
			var index = window.formdesigner_.variables.creatingCtlIndex;
			if (index >= 0) {
				var srcControl = window.formdesigner_.dataModel.optionalControls[index];

				var tgtControl = window.formdesigner_.getControlByPosition(
						window.formdesigner_.activeObject.tempLocation.row,
						window.formdesigner_.activeObject.tempLocation.col);
				// 不许放置目的地有控件存在
				if (tgtControl == null) {
					// 生成目标字段，更新模型
					var ctl = $
							.extend(
									{},
									srcControl,
									{
										row : window.formdesigner_.activeObject.tempLocation.row + 0,
										col : window.formdesigner_.activeObject.tempLocation.col + 0
									});

					srcControl.used = true;

					window.formdesigner_.loadProperty(
							window.formdesigner_.propertyTemplate.common, ctl);
					window.formdesigner_.flags.mouseStartObjType = 'control';
					window.formdesigner_.dataModel.controls.push(ctl);
					window.formdesigner_.activeObject.activeControl = ctl;
				}

			}
		} else if (window.formdesigner_.flags.controlReordering) {
			// 源控件为焦点控件
			var src = window.formdesigner_.activeObject.activeControl;

			var tgt = window.formdesigner_.getControlByPosition(
					window.formdesigner_.activeObject.tempLocation.row,
					window.formdesigner_.activeObject.tempLocation.col);
			// 不许放置目的地有控件存在
			if (tgt == null) {
				src.col = window.formdesigner_.activeObject.tempLocation.col;
				src.row = window.formdesigner_.activeObject.tempLocation.row;
			}
		}
	} else {

		var mx = e.offsetX;
		var my = e.offsetY;

		if (mx > 40 && mx < 170 && my > 330 && my < 450) {
			// 删除控件
			var src = window.formdesigner_.activeObject.activeControl;
			// 删除
			for ( var i in window.formdesigner_.dataModel.controls) {
				if (window.formdesigner_.dataModel.controls[i].name == src.name) {
					window.formdesigner_.dataModel.controls.splice(i, 1);
					break;
				}
			}
			// 还原
			for ( var j in window.formdesigner_.dataModel.optionalControls) {
				if (window.formdesigner_.dataModel.optionalControls[j].name.toUpperCase() == src.name.toUpperCase()) {
					window.formdesigner_.dataModel.optionalControls[j].used = false;
					break;
				}
			}
			// 失去焦点
			window.formdesigner_.activeObject.activeControl = null;
		}

	}

	window.formdesigner_.activeObject.tempLocation.row = null;
	window.formdesigner_.activeObject.tempLocation.col = null;

	window.formdesigner_.flags.mouseStartObjType = 'blank';
	window.formdesigner_.flags.controlCreating = false;
	window.formdesigner_.context.canvas.style.cursor = 'default';
	window.formdesigner_.flags.controlReordering = false;
	window.formdesigner_.flags.trashVisible = false;
	window.formdesigner_.paint();

};

/*******************************************************************************
 * 键盘事件
 ******************************************************************************/
JbfFormDesigner.prototype.designer_canvas_keyDown = function(e) {

};

/*******************************************************************************
 * 修改属性
 ******************************************************************************/
JbfFormDesigner.prototype.changeProperties = function() {
	var grid = $(this.propertyGridId);
	var rows = grid.propertygrid('getRows');
	if (this.activeObject.activeControl.name != null) {
		for ( var i in rows) {
			if (rows[i].field == 'type') {
				var val = rows[i].value;
				for ( var j in this.dataModel.controls) {
					if (this.dataModel.controls[j].name == this.activeObject.activeControl.name) {
						this.dataModel.controls[j].type = val;
						break;
					}
				}

			} else if (rows[i].field == 'colspan') {
				var val = rows[i].value;
				for ( var j in this.dataModel.controls) {
					if (this.dataModel.controls[j].name == this.activeObject.activeControl.name) {
						this.dataModel.controls[j].colspan = val;
						break;
					}
				}
			} else if (rows[i].field == 'notNull') {
				var val = rows[i].value;
				for (var j in this.dataModel.controls) {
					if (this.dataModel.controls[j].name == this.activeObject.activeControl.name) {
						this.dataModel.controls[j].notNull = val;
					}
				}
			} else if (rows[i].field == 'isUnique') {
				var val = rows[i].value;
				for (var j in this.dataModel.controls) {
					if (this.dataModel.controls[j].name == this.activeObject.activeControl.name) {
						this.dataModel.controls[j].isUnique = val;
					}
				}
			}
		}
	}
	this.paint();
};
/*******************************************************************************
 * 修改按钮触发事件
 ******************************************************************************/
function changeProperties() {
	window.formdesigner_.changeProperties();
}
