package com.cjcx.pay.framework.report;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置导出excel的列和字段名
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
	/**
	 * 列名
	 */
	String value() default "";
	
	/**
	 * 列宽
	 */
	int width() default 10;
	
	/**
	 * 日期格式
	 */
	String format() default "yyyy-MM-dd";
	
	/**
	 * 转换器 
	 */
	String converter() default "";
	
	/**
	 *  单元格文本类型
	 */
	String textType() default "";
}
