package com.freework.queryData.servcie;

import java.io.File;
import java.io.IOException;
import java.util.Map;
public interface IQueryConfigParser {
	public Map<String,QueryConfig> loadExceSQL(File file)throws IOException;
	
	
}
