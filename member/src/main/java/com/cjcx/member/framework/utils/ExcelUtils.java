package com.cjcx.member.framework.utils;

import com.cjcx.member.framework.report.ExcelColumn;
import com.cjcx.member.framework.report.ExcelConverter;
import com.cjcx.member.framework.report.ExcelDataDto;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.support.RequestContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 导出excel工具类
 */
public class ExcelUtils {
	
	/**
	 * 通用的导出excel工具类
	 * @param list
	 * @param @ExportColumn注解
	 * @return
	 */
	public static <E> HSSFWorkbook excel(List<ExcelDataDto> list, RequestContext requestContext) {
		HSSFWorkbook wb = new HSSFWorkbook();
		
		if (null == list || list.size() == 0){
			return wb;
		}
		
		HSSFSheet sheet = wb.createSheet("sheet1");
		
		//表头样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Font font = wb.createFont();     
        font.setFontHeightInPoints((short)12);   			//设置字体大小  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);		//设置粗体
        font.setFontName("Courier New");				  	//设置字体  
        style.setFont(font);
        style.setFillForegroundColor((short) 15);			//设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); 	//下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);		//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);		//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);	//右边框
        
        //生成表头
        HSSFRow row = sheet.createRow(Constants.FINAL_INT_ZERO);
        Class<? extends ExcelDataDto> clazz = list.get(Constants.FINAL_INT_ZERO).getClass();
		Field[] fields = BeanUtils.findSuperFields(clazz);
		int cellNum = 0;
		for(int i=0; i<fields.length; i++) {
			ExcelColumn ec = fields[i].getAnnotation(ExcelColumn.class);
			if(ec != null) {
				HSSFCell cell = row.createCell(cellNum);
				sheet.setColumnWidth(cellNum++, ec.width() * 256);
				cell.setCellValue(requestContext.getMessage(ec.value()));
				//cell.setCellValue(ec.value());
				cell.setCellStyle(style);
			}
		}
		
		//HSSFCell.CELL_TYPE_STRING
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		bodyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		bodyStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); 	//下边框
		bodyStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);		//左边框
		bodyStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);		//上边框
		bodyStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);	//右边框
		
		SimpleDateFormat sdf = new SimpleDateFormat();
		for(int i=0; i<list.size(); i++) {
			if(list.get(i) == null) continue;
			row = sheet.createRow(i + 1);
			row.setHeight((short)450);
			
			cellNum = 0;
			for(int j=0; j<fields.length; j++) {
				if(fields[j].isAnnotationPresent(ExcelColumn.class)) {
					HSSFCell cell = row.createCell(cellNum++);
					
					ExcelColumn ec = fields[j].getAnnotation(ExcelColumn.class);
					//上面的CellStyle原来放这里最多4000 styles
					//表主体样式
					if ("NUMERIC".equals(ec.textType())){
						bodyStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));//设置单元类型
						cell.setCellStyle(bodyStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}else{
						cell.setCellStyle(bodyStyle);
					}
					
					if(fields[j].getType().equals(Date.class)) {
						sdf.applyPattern(ec.format());
						try {
							Date d = (Date) BeanUtils.getSuperValue(clazz,fields[j].getName(),list.get(i));
							String dateStr = "";
							if(null != d){
								dateStr = sdf.format(d);
							}
							cell.setCellValue(dateStr);
						} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InvocationTargetException e) {
							cell.setCellValue("");
						}
					} else {
						try {
							String value;
							if(StringUtils.isNotEmpty(ec.converter())) {
								System.out.println("null ec");
								value = requestContext.getMessage(ExcelConverter.convert(ec.converter(), BeanUtils.getSuperValue(clazz,fields[j].getName(),list.get(i))));
							} else {
								value = ConvertUtils.convert(BeanUtils.getSuperValue(clazz,fields[j].getName(),list.get(i)));
							}
							if ("NUMERIC".equals(ec.textType())){
								if(fields[j].getType().getName().equals("java.lang.Integer")){
									cell.setCellValue(Integer.parseInt(StringUtils.defaultString(value)));
								}else if (fields[j].getType().getName().equals("java.lang.Double")){
									cell.setCellValue(Double.parseDouble(StringUtils.defaultString(value)));
								}
							}else{
								cell.setCellValue(StringUtils.defaultString(value));
							}
						} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InvocationTargetException e) {
							cell.setCellValue("");
						}
					}
				}
			}
		}
		
		return wb;
	}
}
