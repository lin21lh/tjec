package com.freework.freedbm.cfg.annotation;

import com.freework.freedbm.cfg.fieldcfg.type.LinkType;
import com.freework.freedbm.cfg.fieldcfg.type.StringLinkName;

public class CreateLinkType {

	static LinkType createLinkType(String str[]){
		String name=str[0];
		if(name.equals("StringLinkName")){
			String linkColName = str[1],  linkValueColName= str[2],
			 sourceName= str[3],
			 tableName= str[4];
			return new StringLinkName(linkColName,  linkValueColName,
					 sourceName,
					 tableName);
		}else
			return null;
		
		
	}
	
}
