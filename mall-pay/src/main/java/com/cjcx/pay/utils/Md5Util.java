package com.cjcx.pay.utils;

import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
//import java.util.Base64;

public class Md5Util {

    private static final String slat = "_m5";

	public Md5Util() {
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(Md5Util.getHexMD5Str("bb123456"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getHexMD5Str(String strIn) throws Exception {
	    strIn = strIn + slat;
		return getHexMD5Str(strIn.getBytes());
	}
	
	public static String getHexMD5Str(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			fis.read(buffer);
			//return getHexMD5Str(Base64.getEncoder().encode((buffer)));
			return getHexMD5Str(Base64Utils.encode(buffer));
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if(fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getHexMD5Str(byte arrIn[]) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte arrB[] = md.digest(arrIn);
		StringBuffer sb = new StringBuffer(32);
		for (int i = 0; i < arrB.length; i++) {
			int intTmp;
			for (intTmp = arrB[i]; intTmp < 0; intTmp += 256);
			if (intTmp < 16)
				sb.append('0');
			//System.out.println(intTmp+" " + Integer.toString(intTmp, 16));
			sb.append(Integer.toString(intTmp, 16));
		}

		return sb.toString();
	}
}
