package com.cjcx.pay.framework.report;

/**
 * 导出Excel数值转换器
 */
public class ExcelConverter {
	/**
	 * 
	 * @param convertor
	 * @param value
	 * @return
	 */
	public static String convert(String convertor, Object value) {
		if( null == value){
			return "";
		}
		
		switch (convertor) {
		case "BOX_STATUS":   /** 设备状态 */
			if (value.equals(0))
				return "EXPORT_000018";
			
		case "SERIAL_PORT_STATUS":   /** 串口状态*/
			if (value.equals(0))
				return "EXPORT_000019";
			else
				return "EXPORT_000020";
			
		case "PARALLEL_PORT_STATUS":   /** 并口状态 */
			if (value.equals(0))
				return "EXPORT_000019";
			else
				return "EXPORT_000020";
			
		case "USB_STATUS":   /** usb口状态*/
			if (value.equals(0))
				return "EXPORT_000019";
			else
				return "EXPORT_000020";
			
		case "NET_STATUS":   /** 网口状态*/
			if (value.equals(0))
				return "EXPORT_000019";
			else
				return "EXPORT_000020";
		
			
		default:
			return value == null ? "" : value.toString();
		}
	}
}
