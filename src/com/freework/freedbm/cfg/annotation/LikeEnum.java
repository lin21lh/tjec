package com.freework.freedbm.cfg.annotation;

import com.freework.freedbm.cfg.fieldcfg.Like;
import com.freework.freedbm.cfg.fieldcfg.Like.Location;

public enum LikeEnum {

	right(Like.RIGHT_LIKE),
	all(Like.ALL_LIKE),
	left(Like.LEFT_LIKE),
	notlike(null);

	Like like=null;
	LikeEnum(Like like){
		this.like=like; 
	}
	LikeEnum(Location location,String wildcard){
		this.like=new Like(location,wildcard); 
	}
	
}
