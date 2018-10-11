JbfFormDesigner.prototype.toXml = function() {

	var xml = new XmlWriter();
	// xml头部编写
	xml.beginnode("config");
	xml.beginnode("layout");
	xml.attrib("col-count", "" + this.dataModel.columnCount);
	xml.attrib("col-width", "" + this.dataModel.controlWidth);
	// -->扩展
	xml.attrib("label-cell-width", "" + this.dataModel.labelCellWidth);
	xml.attrib("control-cell-width", "" + this.dataModel.controlCellWidth);
	// <--扩展结束
	xml.beginnode("fields");
	for ( var i =0 ;i<this.dataModel.controls.length;i++) {
		xml.beginnode("field");
		xml.attrib("name", this.dataModel.controls[i].name);
		//xml.attrib("label", this.dataModel.controls[i].label);
		xml.attrib("merged-cols", this.dataModel.controls[i].colspan ? ""
				+ (2 * this.dataModel.controls[i].colspan - 1) : "1");
		xml.attrib("row", "" + (this.dataModel.controls[i].row + 1));
		xml.attrib("col", "" + (this.dataModel.controls[i].col + 1));
		xml.attrib("not-null",  this.dataModel.controls[i].notNull == "是" ? "1" : "0");
		xml.attrib("is-unique", this.dataModel.controls[i].isUnique == "是" ? "1" : "0");

		xml.beginnode("control");
		xml.attrib("name", this.dataModel.controls[i].type);
		xml.attrib("default-value", "1");

		xml.endnode();// end control
		xml.endnode(); // end field
	}

	xml.endnode();// end fields
	xml.endnode(); // end layout
	xml.endnode();// end config

	var header = '<?xml version="1.0" encoding="UTF-8"?>';
	var xmlContent = header + xml.tostring();
	return xmlContent;

};

JbfFormDesigner.prototype.parseModel = function(xml) {

	// var domParser = new DOMParser();
	// xmlDoc = domParser.parseFromString(xml, 'text/xml');
	var tempDataModel = {
		columnCount : 2,
		labelCellWidth : 100,
		controlCellWidth : 150,
		controlWidth : 140,
		controls : [],
		optionalControls : []
	};
	
	if (!xml) {
		return tempDataModel;
	}
	var $agnt=navigator.userAgent.toLowerCase();
	var config = null, layout = null;
	if($agnt.indexOf("chrome")>0)
		config = xml.childNodes[0];
	else
		config = xml.children[0];
	
	if($agnt.indexOf("chrome")>0)
		layout = config.childNodes[0];
	else
		layout = config.children[0];

	var layout_attr = layout.attributes;
	for ( var attr in layout_attr) {
		switch (layout_attr[attr].name) {
		case 'col-count':
			tempDataModel.columnCount = +layout_attr[attr].value;
			break;
		case 'col-width':
			tempDataModel.controlWidth = +layout_attr[attr].value;
			break;
		case 'label-cell-width':
			tempDataModel.labelCellWidth = +layout_attr[attr].value;
			break;
		case 'control-cell-width':
			tempDataModel.controlCellWidth = +layout_attr[attr].value;
			break;
		default:
			break;
		}
	}

	var fields = null;
	if($agnt.indexOf("chrome")>0)
		fields = layout.childNodes[0].childNodes;
	else
		fields = layout.children[0].children;
	for ( var i in fields) {
		var field = fields[i];
		if (field.nodeName != 'field') {
			continue;
		}
		var c = {};
		var field_attr = field.attributes;
		for ( var k in field_attr) {
			switch (field_attr[k].name) {
			case 'name':
				c.name = field_attr[k].value;
				break;
			case 'label':
				c.label = field_attr[k].value;
				break;
			case 'merged-cols':
				c.colspan = (+field_attr[k].value + 1) / 2;
				break;
			case 'row':
				c.row = (+field_attr[k].value - 1);
				break;
			case 'col':
				c.col = (+field_attr[k].value - 1);
				break;
			case 'not-null':
				c.notNull = field_attr[k].value == 1 ? "是" : "否";
				break;
			case 'is-unique':
				c.isUnique = field_attr[k].value == 1 ? "是" : "否";
				break;
			}
		}
		var control = null;
		if($agnt.indexOf("chrome")>0)
			control = field.childNodes[0];
		else
			control = field.children[0];
		if (control) {
			for ( var kk in control.attributes) {
				switch (control.attributes[kk].name) {
				case 'name':
					c.type = control.attributes[kk].value;
					break;
				case 'default-value':
					c.defaultValue = control.attributes[kk].value;
					break;

				}
			}
		}
		tempDataModel.controls.push(c);
	}

	return tempDataModel;

};
