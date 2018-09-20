package com.cjcx.member.framework.annotation;

import java.lang.annotation.*;


/*
 * 不验证Token注解
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthToken {
	
}
