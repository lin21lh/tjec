function XmlWriter() {
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