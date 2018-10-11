package com.freework.freedbm.cfg.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented  

public @interface Field {
	 String colName();
	 String comments() default "";
	 String[] linkType() default {} ;
	 boolean isKey() default false;
	 LikeEnum like() default LikeEnum.notlike;
	 String defVal() default "";
	
	
}
