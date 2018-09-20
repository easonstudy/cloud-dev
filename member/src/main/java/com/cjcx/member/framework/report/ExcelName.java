package com.cjcx.member.framework.report;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置导出excel的文件名
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelName {
	/** 文件名 */
	String value() default "导出文件_yyyy-MM-dd_HH:mm:ss";
	
	/** 扩展名 */
	String extension() default ".xls";
}
