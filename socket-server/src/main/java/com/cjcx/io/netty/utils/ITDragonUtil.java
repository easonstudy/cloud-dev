package com.cjcx.io.netty.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.ByteArrayInputStream;  
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;  
import java.util.zip.GZIPOutputStream;

public class ITDragonUtil {

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		String a = "function d(a, b){ return a+b; } \n d(1,2)";
		for (int i = 0; i < 100; i++) {
			System.out.println("JavaScript" + i +" : " + cal(a));
		}

		System.out.println("用时:" + (System.currentTimeMillis() -s ));
		s= System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			System.out.println("Nashorn" + i +" : " + cal2(a));
		}
		System.out.println("用时:" + (System.currentTimeMillis() -s ));


	}

	private final static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
	private final static ScriptEngine jse2 = new ScriptEngineManager().getEngineByName("nashorn");
    public static Object cal(String expression){
        try {
			return jse.eval(expression);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
        return null;

    }
    public static Object cal2(String expression){
        try {
			return jse2.eval(expression);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
        return null;
    }
    
    public static byte[] gzip(byte[] data){  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(bos);
			gzip.write(data);  
			gzip.finish();  
			gzip.close();  
			byte[] ret = bos.toByteArray();  
			bos.close();  
			return ret;  
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return null;
    }  
      
    public static byte[] ungzip(byte[] data){  
        ByteArrayInputStream bis = new ByteArrayInputStream(data);  
        GZIPInputStream gzip;
		try {
			gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];  
			int num = -1;  
			ByteArrayOutputStream bos = new ByteArrayOutputStream();  
			while((num = gzip.read(buf, 0 , buf.length)) != -1 ){  
				bos.write(buf, 0, num);  
			}  
			gzip.close();  
			bis.close();  
			byte[] ret = bos.toByteArray();  
			bos.flush();  
			bos.close();  
			return ret;  
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
    }  

}
