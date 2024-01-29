package com.pwc.pwcesg.frontoffice.search.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Image;
import java.net.URLDecoder;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class CryptoUtil  {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	private String getKey() {
		return "djkflsdfjl+)(*!@";
	}

	public String encryptAES(String codedID) {
		if ((codedID != null) && (codedID.length() != 0)) {
			Key keySpec = new SecretKeySpec(getKey().getBytes(), "AES");
			String instance = "AES/ECB/ISO10126Padding";
			String outputStr1 = "";
			try {
				Cipher cipher = Cipher.getInstance(instance);
				cipher.init(1, keySpec);
				byte[] inputBytes1 = codedID.getBytes("UTF8");
				byte[] outputBytes1 = cipher.doFinal(inputBytes1);

				Encoder encoder = Base64.getEncoder();
				outputStr1 = encoder.encode(outputBytes1).toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return outputStr1;
		}
		return "";
	}

	public String decryptAES(String codedID) {
		if ((codedID != null) && (codedID.length() != 0)) {
			Key keySpec = new SecretKeySpec(getKey().getBytes(), "AES");
			String instance = "AES/ECB/ISO10126Padding";
			String strResult = "";			
			try {
				Cipher cipher = Cipher.getInstance(instance);
				cipher.init(2, keySpec);
				Decoder decoder = Base64.getDecoder();
				//byte[] inputBytes1 = decoder.decode(codedID.replace(" ","+"));
				//byte[] inputBytes1 = decoder.decode(URLDecoder.decode(codedID,"UTF-8"));
				byte[] inputBytes1 = decoder.decode(codedID);
				byte[] outputBytes2 = cipher.doFinal(inputBytes1);
				strResult = new String(outputBytes2, "UTF8");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return strResult;
		}
		return "";
	}

}



