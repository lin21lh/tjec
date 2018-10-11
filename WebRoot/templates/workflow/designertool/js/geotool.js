function inline(x, y, x1, y1, x2, y2) {
	
	if (x1 - x2 == 0) {
		// 竖直
		if (y <= Math.max(y1, y2) && y > Math.min(y1, y2)) {
			if (Math.abs(x - x1) <= 2) {
				return true;
			}
		}
		return false;
	}

	var k = (y1 - y2) / (x1 - x2);

	var b = y1 - k * x1;

	var resy = k * x + b;
	
	var resx= (y-b)/k;
	console.log("result suby ="+(resy - y));
	console.log("result subx ="+(resx - x));
	if (Math.abs(resy - y) <= 5||Math.abs(resx - x)<=5) {
		if (y <= Math.max(y1, y2)+3 && y >= Math.min(y1, y2)-3
				&& x <= Math.max(x1, x2)+3 && x >= Math.min(x1, x2)-3) {
			return true;
		}
	}
	
	
	return false;
}